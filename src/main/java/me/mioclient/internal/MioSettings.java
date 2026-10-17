package me.mioclient.internal;

import me.mioclient.module.Module;

/**
 * Reflection-based replacement for {@code me.mioclient.nick.Settings.initialize(this)} from the
 * loader jar. The loader used encrypted metadata (SettingNames / SettingValues /
 * SettingPredicates) to reflectively create and assign the {@code Setting<?>} fields
 * declared on each module during construction. That jar is no longer required to
 * build: this class recreates the same behaviour from the plaintext field-name
 * mapping in {@link MioSettingNames} plus sensible per-type defaults.
 *
 * <p>Only fields that are still {@code null} are populated, so modules that assign
 * their settings inline (e.g. {@code public final Setting<Boolean> x = this.add(...)})
 * are left untouched. Population runs during the owning constructor, so code that uses
 * the setting fields right after {@code Settings.initialize(this)} sees them ready.
 *
 * <p>The heavier "shell module" repair (filling arbitrary null List/Map/mio fields for
 * modules whose constructor threw) still lives in {@link MioModules}; this class handles
 * only the {@code Setting<?>} fields, which is exactly what the loader's initialize did.
 */
public final class MioSettings {
   private MioSettings() {
      super();
   }

   private static volatile sun.misc.Unsafe UNSAFE;
   private static volatile boolean UNSAFE_TRIED;

   private static sun.misc.Unsafe unsafe() {
      if (UNSAFE_TRIED) return UNSAFE;
      synchronized (MioSettings.class) {
         if (!UNSAFE_TRIED) {
            try {
               java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
               f.setAccessible(true);
               UNSAFE = (sun.misc.Unsafe) f.get(null);
            } catch (Throwable t) {
               UNSAFE = null;
            }
            UNSAFE_TRIED = true;
         }
      }
      return UNSAFE;
   }

   /** Entry point invoked by {@code me.mioclient.nick.Settings.initialize(this)} in every module constructor. */
   public static void initialize(Object module) {
      if (module == null) return;
      populate(module, unsafe());
      if (module instanceof Module m) {
         orderRegistry(m);
      }
   }

   /** Populate every still-null {@code Setting<?>} field on {@code m} and register it. */
   static void populate(Object m, sun.misc.Unsafe unsafe) {
      Class<?> klass = m.getClass();
      while (klass != null && klass != Object.class) {
         for (java.lang.reflect.Field f : klass.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
            if (!me.mioclient.setting.Setting.class.isAssignableFrom(f.getType())) continue;
            try {
               f.setAccessible(true);
               if (f.get(m) != null) continue;
               me.mioclient.setting.Setting<?> val = makeSetting(f);
               if (val == null) continue;
               if (java.lang.reflect.Modifier.isFinal(f.getModifiers()) && unsafe != null) {
                  unsafe.putObject(m, unsafe.objectFieldOffset(f), val);
               } else {
                  f.set(m, val);
               }
               if (m instanceof Module mod) {
                  java.util.List<me.mioclient.setting.Setting<?>> reg = mod.getRegistry();
                  if (reg != null && !reg.contains(val)) reg.add(val);
               }
            } catch (Throwable t) {
               // ignore: leave the field null
            }
         }
         klass = klass.getSuperclass();
      }
   }

   /**
    * Original constructors register settings in declaration order, superclass fields first.
    * Reflection visits subclass fields first, so re-sort the registry to match.
    */
   static void orderRegistry(Module m) {
      try {
         java.util.List<me.mioclient.setting.Setting<?>> reg = m.getRegistry();
         if (reg == null || reg.size() <= 1) return;
         java.util.List<Class<?>> chain = new java.util.ArrayList<>();
         for (Class<?> c = m.getClass(); c != null && c != Object.class; c = c.getSuperclass()) chain.add(0, c);
         java.util.Map<me.mioclient.setting.Setting<?>, Integer> order = new java.util.IdentityHashMap<>();
         int idx = 0;
         for (Class<?> c : chain) {
            for (java.lang.reflect.Field f : c.getDeclaredFields()) {
               if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
               if (!me.mioclient.setting.Setting.class.isAssignableFrom(f.getType())) continue;
               try {
                  f.setAccessible(true);
                  Object v = f.get(m);
                  if (v instanceof me.mioclient.setting.Setting<?> s) order.put(s, idx++);
               } catch (Throwable ignore) {}
            }
         }
         reg.sort(java.util.Comparator.comparingInt(s -> order.getOrDefault(s, Integer.MAX_VALUE)));
      } catch (Throwable ignore) {}
   }

   private static volatile java.util.Map<String, String> SETTING_NAME_LOOKUP;

   private static java.util.Map<String, String> settingNameLookup() {
      java.util.Map<String, String> snap = SETTING_NAME_LOOKUP;
      if (snap != null) return snap;
      snap = new java.util.HashMap<>();
      try {
         // MioSettingNames is a plaintext clone of the deobf setting metadata. Keys read as
         // "me.mioclient.deobf.Class_XXXX.<field>"; we slice off the field-only suffix. Setting
         // fields that had a recovered name were since renamed from field_YYYY to that name, so
         // for those the suffix is the real identifier (e.g. "radius") mapping to its display
         // label ("Radius"); the remaining unrecovered ones still read as "field_YYYY".
         java.util.Map<String, String> raw = MioSettingNames.asMap();
         for (var e : raw.entrySet()) {
            String key = e.getKey();
            int dot = key.lastIndexOf('.');
            String fname = dot < 0 ? key : key.substring(dot + 1);
            // last writer wins is fine — field names are globally unique in the mapping
            snap.put(fname, e.getValue());
         }
      } catch (Throwable t) {
         // fall back to raw field names
      }
      SETTING_NAME_LOOKUP = snap;
      return snap;
   }

   private static String prettyFieldName(String raw) {
      String real = settingNameLookup().get(raw);
      return real == null ? raw : real;
   }

   static me.mioclient.setting.Setting<?> makeSetting(java.lang.reflect.Field f) {
      String name = prettyFieldName(f.getName());
      Class<?> t = extractGenericArg(f);
      if (t == Boolean.class) return new me.mioclient.setting.BooleanSetting(name, false);
      if (t == String.class) return new me.mioclient.setting.StringSetting(name, "");
      if (t == java.awt.Color.class) return new me.mioclient.setting.ColorSetting(name, defaultColorFor(name));
      // default to 1 (not 0) — many number settings are scales/multipliers and a 0 default
      // collapses click-GUI scale to zero (matrix.scale(0,0,1)) → nothing renders
      if (t == Integer.class) return new me.mioclient.setting.CustomSetting3<>(name, 1, 0, 100);
      if (t == Double.class) return new me.mioclient.setting.CustomSetting3<>(name, 1.0, 0.0, 10.0);
      if (t == Float.class) return new me.mioclient.setting.CustomSetting3<>(name, 1.0f, 0.0f, 10.0f);
      if (t == Long.class) return new me.mioclient.setting.CustomSetting3<>(name, 1L, 0L, 100L);
      if (t == me.mioclient.internal.SoundId.class) return new me.mioclient.setting.Class0211Setting(name, new me.mioclient.internal.SoundId(""));
      if (t != null && t.isEnum()) {
         return makeEnumSetting(name, t);
      }
      // Collection-typed settings: Setting<Set<X>> / Setting<List<X>> etc.
      // Hand back an empty collection so the field is non-null and registered in the GUI.
      if (t != null && java.util.Collection.class.isAssignableFrom(t)) {
         java.util.Collection<Object> empty;
         if      (java.util.Set.class.isAssignableFrom(t))  empty = new java.util.LinkedHashSet<>();
         else if (java.util.Deque.class.isAssignableFrom(t) || java.util.Queue.class.isAssignableFrom(t)) empty = new java.util.ArrayDeque<>();
         else                                                empty = new java.util.ArrayList<>();
         @SuppressWarnings({"unchecked","rawtypes"})
         me.mioclient.setting.Setting<?> s =
               new me.mioclient.setting.CollectionSetting(name, (java.util.Collection) empty);
         return s;
      }
      return null;
   }

   private static java.awt.Color defaultColorFor(String name) {
      // give the click GUI sensible default colors so it shows up against the white-tinted screen background
      String lower = name.toLowerCase();
      if (lower.contains("text") || lower.contains("name") || lower.contains("title") || lower.contains("font")) {
         return new java.awt.Color(0xFFFFFF);
      }
      if (lower.contains("accent") || lower.contains("highlight") || lower.contains("active") || lower.contains("enabled")) {
         return new java.awt.Color(0x6CA0DC);
      }
      if (lower.contains("border") || lower.contains("outline")) {
         return new java.awt.Color(0x202020);
      }
      // default: dark grey panel/body background
      return new java.awt.Color(0x202020);
   }

   @SuppressWarnings({"unchecked","rawtypes"})
   private static <E extends Enum<E>> me.mioclient.setting.Setting<?> makeEnumSetting(String name, Class<?> t) {
      E first = (E) t.getEnumConstants()[0];
      return new me.mioclient.setting.CustomSetting(name, first);
   }

   private static Class<?> extractGenericArg(java.lang.reflect.Field f) {
      java.lang.reflect.Type g = f.getGenericType();
      if (g instanceof java.lang.reflect.ParameterizedType pt) {
         java.lang.reflect.Type[] args = pt.getActualTypeArguments();
         if (args.length > 0) {
            java.lang.reflect.Type a = args[0];
            if (a instanceof Class<?> c) return c;
            // Nested generic like Setting<Set<Block>> — drop down to the raw container type.
            if (a instanceof java.lang.reflect.ParameterizedType inner && inner.getRawType() instanceof Class<?> raw) return raw;
         }
      }
      return null;
   }
}
