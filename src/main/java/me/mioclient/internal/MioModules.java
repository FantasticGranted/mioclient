package me.mioclient.internal;

import me.mioclient.module.Module;

public final class MioModules {
   public static void init(Class_0965 manager) {
      String[] classes = new String[] {
         "me.mioclient.module.abstract_.AbstractModule_10",
         "me.mioclient.module.abstract_.AbstractModule_1",
         "me.mioclient.module.abstract_.AbstractModule_11",
         "me.mioclient.module.abstract_.AbstractModule_12",
         "me.mioclient.module.abstract_.AbstractModule_13",
         "me.mioclient.module.abstract_.AbstractModule_15",
         "me.mioclient.module.abstract_.AbstractModule_16",
         "me.mioclient.module.abstract_.AbstractModule_17",
         "me.mioclient.module.abstract_.AbstractModule_2",
         "me.mioclient.module.abstract_.AbstractModule_20",
         "me.mioclient.module.abstract_.AbstractModule_14",
         "me.mioclient.module.abstract_.AbstractModule_23",
         "me.mioclient.module.abstract_.AbstractModule_19",
         "me.mioclient.module.abstract_.AbstractModule_18",
         "me.mioclient.module.abstract_.AbstractModule_24",
         "me.mioclient.module.abstract_.AbstractModule_27",
         "me.mioclient.module.abstract_.AbstractModule_21",
         "me.mioclient.module.abstract_.AbstractModule_22",
         "me.mioclient.module.abstract_.AbstractModule_25",
         "me.mioclient.module.abstract_.AbstractModule_30",
         "me.mioclient.module.abstract_.AbstractModule_28",
         "me.mioclient.module.abstract_.AbstractModule_3",
         "me.mioclient.module.abstract_.AbstractModule_29",
         "me.mioclient.module.abstract_.AbstractModule_34",
         "me.mioclient.module.abstract_.AbstractModule_33",
         "me.mioclient.module.abstract_.AbstractModule_38",
         "me.mioclient.module.abstract_.AbstractModule_31",
         "me.mioclient.module.abstract_.AbstractModule_7",
         "me.mioclient.module.abstract_.AbstractModule_35",
         "me.mioclient.module.abstract_.AbstractModule_37",
         "me.mioclient.module.abstract_.AbstractModule_40",
         "me.mioclient.module.abstract_.AbstractModule_6",
         "me.mioclient.module.abstract_.AbstractModule_4",
         "me.mioclient.module.abstract_.AbstractModule_36",
         "me.mioclient.module.abstract_.AbstractModule_42",
         "me.mioclient.module.abstract_.AbstractModule_5",
         "me.mioclient.module.abstract_.AbstractModule_9",
         "me.mioclient.module.abstract_.AbstractModule_39",
         "me.mioclient.module.abstract_.AbstractModule_8",
         "me.mioclient.module.client.DiscordModule",
         "me.mioclient.module.client.FontsModule",
         "me.mioclient.module.client.HUDModule",
         "me.mioclient.module.client.IRCModule",
         "me.mioclient.module.client.NotificationsModule",
         "me.mioclient.module.client.UIModule",
         "me.mioclient.module.combat.AimAssistModule",
         "me.mioclient.module.combat.AntiBotModule",
         "me.mioclient.module.combat.AntiPhaseModule",
         "me.mioclient.module.combat.ArrowsModule",
         "me.mioclient.module.combat.AuraModule",
         "me.mioclient.module.combat.AutoArmorModule",
         "me.mioclient.module.combat.AutoBowReleaseModule",
         "me.mioclient.module.combat.AutoClickerModule",
         "me.mioclient.module.combat.AutoCrystalModule",
         "me.mioclient.module.combat.AutoExpModule",
         "me.mioclient.module.combat.AutoLogModule",
         "me.mioclient.module.combat.BowAimModule",
         "me.mioclient.module.combat.CombatmineModule",
         "me.mioclient.module.combat.CriticalsModule",
         "me.mioclient.module.combat.KeyPearlModule",
         "me.mioclient.module.combat.LegacyCrystalModule",
         "me.mioclient.module.combat.MainhandModule",
         "me.mioclient.module.combat.OffhandModule",
         "me.mioclient.module.combat.PusherModule",
         "me.mioclient.module.combat.SelfFillModule",
         "me.mioclient.module.combat.SelfWebModule",
         "me.mioclient.module.combat.TriggerModule",
         "me.mioclient.module.exploit.AntiHungerModule",
         "me.mioclient.module.exploit.AntiLevitationModule",
         "me.mioclient.module.exploit.AntiVanishModule",
         "me.mioclient.module.exploit.FakeVanillaModule",
         "me.mioclient.module.exploit.FastLatencyModule",
         "me.mioclient.module.exploit.FastProjectileModule",
         "me.mioclient.module.exploit.GrimDisablerModule",
         "me.mioclient.module.exploit.HitboxDesyncModule",
         "me.mioclient.module.exploit.IllegalDisconnectModule",
         "me.mioclient.module.exploit.MultiTaskModule",
         "me.mioclient.module.exploit.NewChunksModule",
         "me.mioclient.module.exploit.NoMineAnimationModule",
         "me.mioclient.module.exploit.PhaseModule",
         "me.mioclient.module.exploit.PingSpoofModule",
         "me.mioclient.module.exploit.PortalGodModeModule",
         "me.mioclient.module.exploit.ReachModule",
         "me.mioclient.module.exploit.RocketModule",
         "me.mioclient.module.exploit.TimerModule",
         "me.mioclient.module.exploit.XCarryModule",
         "me.mioclient.module.misc.AnnouncerModule",
         "me.mioclient.module.misc.AntiAFKModule",
         "me.mioclient.module.misc.AntiAimModule",
         "me.mioclient.module.misc.AntiCyrillicModule",
         "me.mioclient.module.misc.AntiQuitModule",
         "me.mioclient.module.misc.AntiSpamModule",
         "me.mioclient.module.misc.AutoReconnectModule",
         "me.mioclient.module.misc.AutoRespawnModule",
         "me.mioclient.module.misc.AutoSignModule",
         "me.mioclient.module.misc.BetterChatModule",
         "me.mioclient.module.misc.ChatFilterModule",
         "me.mioclient.module.misc.ChestSearchBarModule",
         "me.mioclient.module.misc.CoordLoggerModule",
         "me.mioclient.module.misc.CustomDeathTextModule",
         "me.mioclient.module.misc.DiscordNotifsModule",
         "me.mioclient.module.misc.ExtraScreenshotModule",
         "me.mioclient.module.misc.ExtraTabModule",
         "me.mioclient.module.misc.HeavenModule",
         "me.mioclient.module.misc.KillEffectsModule",
         "me.mioclient.module.misc.MiddleClickModule",
         "me.mioclient.module.misc.NoNarratorModule",
         "me.mioclient.module.misc.NoPacketKickModule",
         "me.mioclient.module.misc.NoRotateModule",
         "me.mioclient.module.misc.PMSoundModule",
         "me.mioclient.module.misc.SkinFlickerModule",
         "me.mioclient.module.misc.SoundBlockerModule",
         "me.mioclient.module.misc.SpammerModule",
         "me.mioclient.module.misc.StashFinderModule",
         "me.mioclient.module.misc.SwingModule",
         "me.mioclient.module.misc.UnfocusedFPSModule",
         "me.mioclient.module.misc.VisualRangeModule",
         "me.mioclient.module.movement.AntiVoidModule",
         "me.mioclient.module.movement.AutoWalkModule",
         "me.mioclient.module.movement.ElytraFlyModule",
         "me.mioclient.module.movement.EntityControlModule",
         "me.mioclient.module.movement.FakeLagModule",
         "me.mioclient.module.movement.FastFallModule",
         "me.mioclient.module.movement.FastLadderModule",
         "me.mioclient.module.movement.FastSwimModule",
         "me.mioclient.module.movement.FastWebModule",
         "me.mioclient.module.movement.FireworksModule",
         "me.mioclient.module.movement.FlightModule",
         "me.mioclient.module.movement.HighJumpModule",
         "me.mioclient.module.movement.HoleSnapModule",
         "me.mioclient.module.movement.JesusModule",
         "me.mioclient.module.movement.LongJumpModule",
         "me.mioclient.module.movement.NoFallModule",
         "me.mioclient.module.movement.NoJumpDelayModule",
         "me.mioclient.module.movement.NoSlowModule",
         "me.mioclient.module.movement.SafeWalkModule",
         "me.mioclient.module.movement.SpeedModule",
         "me.mioclient.module.movement.SpiralModule",
         "me.mioclient.module.movement.SprintModule",
         "me.mioclient.module.movement.StepModule",
         "me.mioclient.module.movement.VelocityModule",
         "me.mioclient.module.movement.WarpModule",
         "me.mioclient.module.player.AutoBreedModule",
         "me.mioclient.module.player.AutoCraftModule",
         "me.mioclient.module.player.AutoEatModule",
         "me.mioclient.module.player.AutoFarmModule",
         "me.mioclient.module.player.AutoFishModule",
         "me.mioclient.module.player.AutoMountModule",
         "me.mioclient.module.player.AutoNameTagModule",
         "me.mioclient.module.player.AutoTameModule",
         "me.mioclient.module.player.AutoToolModule",
         "me.mioclient.module.player.AutoUpgradeModule",
         "me.mioclient.module.player.BlockMixerModule",
         "me.mioclient.module.player.ChestStealerModule",
         "me.mioclient.module.player.FastPlaceModule",
         "me.mioclient.module.player.FreecamModule",
         "me.mioclient.module.player.InventoryCleanerModule",
         "me.mioclient.module.player.InventoryTweaksModule",
         "me.mioclient.module.player.ItemSaverModule",
         "me.mioclient.module.player.NameProtectModule",
         "me.mioclient.module.player.NoInteractModule",
         "me.mioclient.module.player.NukerModule",
         "me.mioclient.module.player.ReplenishModule",
         "me.mioclient.module.player.RotationLockModule",
         "me.mioclient.module.player.ScaffoldModule",
         "me.mioclient.module.player.SpeedMineModule",
         "me.mioclient.module.render.AmbienceModule",
         "me.mioclient.module.render.AnimationsModule",
         "me.mioclient.module.render.BlurModule",
         "me.mioclient.module.render.BordersModule",
         "me.mioclient.module.render.BreakHighlightModule",
         "me.mioclient.module.render.ChamsModule",
         "me.mioclient.module.render.CrosshairModule",
         "me.mioclient.module.render.ESPModule",
         "me.mioclient.module.render.FreeLookModule",
         "me.mioclient.module.render.GlintModule",
         "me.mioclient.module.render.HighlightModule",
         "me.mioclient.module.render.HitmarkerModule",
         "me.mioclient.module.render.HoleESPModule",
         "me.mioclient.module.render.LogoutSpotsModule",
         "me.mioclient.module.render.MarkersModule",
         "me.mioclient.module.render.NameTagsModule",
         "me.mioclient.module.render.NoBobModule",
         "me.mioclient.module.render.NoRenderModule",
         "me.mioclient.module.render.ParticlesModule",
         "me.mioclient.module.render.PhaseESPModule",
         "me.mioclient.module.render.SearchModule",
         "me.mioclient.module.render.ShaderModule",
         "me.mioclient.module.render.SkeletonModule",
         "me.mioclient.module.render.SkyColorModule",
         "me.mioclient.module.render.TracersModule",
         "me.mioclient.module.render.TrailsModule",
         "me.mioclient.module.render.TrajectoriesModule",
         "me.mioclient.module.render.TunnelsModule",
         "me.mioclient.module.render.ViewClipModule",
         "me.mioclient.module.render.ViewModelModule",
         "me.mioclient.module.render.VoidESPModule",
         "me.mioclient.module.render.WaypointsModule",
         "me.mioclient.module.render.XrayModule",
         "me.mioclient.module.render.ZoomModule",
      };
      sun.misc.Unsafe unsafe = null;
      try {
         java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
         f.setAccessible(true);
         unsafe = (sun.misc.Unsafe) f.get(null);
      } catch (Throwable t) {
         System.err.println("[Mio] Cannot get Unsafe: " + t);
      }
      int ok = 0, shells = 0, failed = 0;
      for (String name : classes) {
         try {
            Class<?> klass = Class.forName(name);
            Module m;
            try {
               java.lang.reflect.Constructor<?> ctor = klass.getDeclaredConstructor();
               ctor.setAccessible(true);
               m = (Module) ctor.newInstance();
               ok++;
            } catch (Throwable ctorErr) {
               if (unsafe == null) throw ctorErr;
               m = (Module) unsafe.allocateInstance(klass);
               for (java.lang.reflect.Field sf : klass.getDeclaredFields()) {
                  if (java.lang.reflect.Modifier.isStatic(sf.getModifiers()) && sf.getType().equals(klass)) {
                     unsafe.putObject(klass, unsafe.staticFieldOffset(sf), m);
                  }
               }
               shells++;
            }
            // populate any still-null Setting<?> fields with sensible defaults so downstream getValue() doesn't NPE
            populateSettings(m, unsafe);
            populateCategory(m, klass, unsafe);
            populateName(m, klass, unsafe);
            manager.method_29(m);
         } catch (Throwable t) {
            failed++;
            System.err.println("[Mio] Failed to load module: " + name + " - " + t);
         }
      }
      System.err.println("[Mio] Modules: " + ok + " full, " + shells + " shell, " + failed + " failed");
      applyDefaultKeybinds(manager);
      applyUIModuleDefaults(manager, unsafe);
   }

   private static void applyUIModuleDefaults(Class_0965 manager, sun.misc.Unsafe unsafe) {
      try {
         me.mioclient.module.client.UIModule ui = manager.method_78(me.mioclient.module.client.UIModule.class);
         if (ui == null) return;
         // Sized fields (padding, panel width, etc.) need real numeric defaults so the click GUI
         // has nonzero geometry. Set each via the typed setter so dependent recalculation runs.
         if (ui.buttonHeight != null) ui.buttonHeight.method_78(Integer.valueOf(2));   // scroll padding
         if (ui.frameWidth != null) ui.frameWidth.method_78(Integer.valueOf(0));   // panel width extra (original = 0)
         if (ui.modulePadding != null) ui.modulePadding.method_78(Integer.valueOf(2));   // text x padding
         if (ui.delay != null) ui.delay.method_78(Integer.valueOf(400)); // tooltip delay ms
         if (ui.animSpeed != null) ui.animSpeed.method_78(Float.valueOf(1.0f));
         if (ui.guiScale != null) ui.guiScale.method_78(Float.valueOf(1.0f));
         if (ui.shadowSize != null) ui.shadowSize.method_78(Float.valueOf(1.0f));
         if (ui.volume != null) ui.volume.method_78(Float.valueOf(1.0f));
         if (ui.volume2 != null) ui.volume2.method_78(Float.valueOf(1.0f));
         if (ui.volume3 != null) ui.volume3.method_78(Float.valueOf(1.0f));
         // Match the mio reference: saturated purple header, translucent lavender body,
         // transparent-OFF / darker-ON row backgrounds, light text. Game stays visible behind.
         java.awt.Color textOff = new java.awt.Color(235, 230, 245, 255);
         java.awt.Color textOn  = new java.awt.Color(255, 255, 255, 255);
         java.awt.Color header  = new java.awt.Color(112,  88, 152, 230);
         java.awt.Color body    = new java.awt.Color(200, 176, 232, 110);
         java.awt.Color rowOff  = new java.awt.Color(  0,   0,   0,   0);
         java.awt.Color rowOn   = new java.awt.Color( 96,  64, 144, 180);
         java.awt.Color scrnBg  = new java.awt.Color(  0,   0,   0,   0);
         java.awt.Color scrnFg  = new java.awt.Color(  0,   0,   0,  40);
         java.awt.Color border  = new java.awt.Color( 64,  48,  96, 220);
         java.awt.Color shadow  = new java.awt.Color(  0,   0,   0, 100);
         if (ui.shadowColor != null) ui.shadowColor.method_78(shadow);
         if (ui.textColor != null) ui.textColor.method_78(textOff);
         if (ui.enabledColor != null) ui.enabledColor.method_78(textOn);
         if (ui.color != null) ui.color.method_78(header);
         if (ui.bgColor != null) ui.bgColor.method_78(body);
         if (ui.bgButton != null) ui.bgButton.method_78(rowOff);
         if (ui.bgEnabled != null) ui.bgEnabled.method_78(rowOn);
         if (ui.tint != null) ui.tint.method_78(scrnBg);
         if (ui.gradientColor != null) ui.gradientColor.method_78(scrnFg);
         if (ui.line != null) ui.line.method_78(Boolean.TRUE);  // Line
         if (ui.binds != null) ui.binds.method_78(Boolean.TRUE);  // Binds — show [KEY] suffix
         if (ui.gear != null) ui.gear.method_78(Boolean.TRUE);  // Gear — show + expand indicator
         if (ui.descriptions != null) ui.descriptions.method_78(Boolean.TRUE);  // Descriptions — tooltip on hover
         if (ui.elements != null) ui.elements.method_78(Boolean.TRUE);  // Elements
         if (ui.colors != null) ui.colors.method_78(Boolean.TRUE);  // Colors
         if (ui.sounds != null) ui.sounds.method_78(Boolean.FALSE); // Sounds — silence by default
         if (ui.windowShadow != null) ui.windowShadow.method_78(Boolean.FALSE); // WindowShadow
         if (ui.snow != null) ui.snow.method_78(Boolean.FALSE); // Snow
         if (ui.prefix != null) ui.prefix.method_78(";");
         System.err.println("[Mio] UIModule defaults applied");
      } catch (Throwable t) {
         System.err.println("[Mio] applyUIModuleDefaults: " + t);
         t.printStackTrace(System.err);
      }
   }

   private static void applyDefaultKeybinds(Class_0965 manager) {
      try {
         me.mioclient.module.client.UIModule ui = manager.method_78(me.mioclient.module.client.UIModule.class);
         if (ui != null && (ui.getKeybind() == null || ui.getKeybind().method_38() < 0)) {
            ui.setKeybind(new me.mioclient.record.Keybind(org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT_SHIFT, me.mioclient.enum_.BindType.TOGGLE, false));
            System.err.println("[Mio] UIModule keybind set to RIGHT_SHIFT (344)");
         }
      } catch (Throwable t) {
         System.err.println("[Mio] applyDefaultKeybinds: " + t);
      }
   }

   private static void populateName(Module m, Class<?> klass, sun.misc.Unsafe unsafe) {
      if (unsafe == null) return;
      try {
         if (m.getName() != null && !m.getName().isEmpty()) return;
         // Prefer the real super("RealName", ...) string baked into the constructor bytecode.
         String real = CtorNameSniffer.sniff(klass);
         String simple;
         if (real != null && !real.isEmpty()) {
            simple = real;
         } else {
            simple = klass.getSimpleName();
            if (simple.endsWith("Module")) simple = simple.substring(0, simple.length() - 6);
         }
         java.lang.reflect.Field f = me.mioclient.deobf.Named.class.getDeclaredField("field_327");
         unsafe.putObject(m, unsafe.objectFieldOffset(f), simple);
         // Named.field_2189 (description) is normally initialized to "" by the instance field
         // initializer in the source. Unsafe.allocateInstance skips initializers, so for shell
         // modules it stays null. Anyone calling getDescription().split(...) NPEs (the chat
         // help command does this). Write "" via Unsafe to match the source default.
         try {
            java.lang.reflect.Field df = me.mioclient.deobf.Named.class.getDeclaredField("field_2189");
            if (df.get(m) == null) unsafe.putObject(m, unsafe.objectFieldOffset(df), "");
         } catch (Throwable ignoreDesc) {}
         java.lang.reflect.Field af = Module.class.getDeclaredField("field_39");
         if (af.get(m) == null) {
            af.setAccessible(true);
            af.set(m, new String[]{ simple });
         }
      } catch (Throwable ignore) {}
   }

   private static void populateCategory(Module m, Class<?> klass, sun.misc.Unsafe unsafe) {
      if (unsafe == null) return;
      try {
         if (m.getCategory() != null) return;
         String pkg = klass.getName();
         me.mioclient.module.Category cat = null;
         if (pkg.contains(".combat.")) cat = me.mioclient.module.Category.COMBAT;
         else if (pkg.contains(".misc.")) cat = me.mioclient.module.Category.MISC;
         else if (pkg.contains(".render.")) cat = me.mioclient.module.Category.RENDER;
         else if (pkg.contains(".movement.")) cat = me.mioclient.module.Category.MOVEMENT;
         else if (pkg.contains(".player.")) cat = me.mioclient.module.Category.PLAYER;
         else if (pkg.contains(".exploit.")) cat = me.mioclient.module.Category.EXPLOIT;
         else if (pkg.contains(".client.")) cat = me.mioclient.module.Category.CLIENT;
         else if (pkg.contains(".abstract_.")) cat = me.mioclient.module.Category.MISC; // abstract HUD/widget modules
         if (cat == null) return;
         Class<?> mc = Module.class;
         java.lang.reflect.Field f = mc.getDeclaredField("field_37");
         unsafe.putObject(m, unsafe.objectFieldOffset(f), cat);
      } catch (Throwable ignore) {}
   }

   private static void populateSettings(Module m, sun.misc.Unsafe unsafe) {
      // Setting<?> fields are handled by the shared MioSettings machinery (the same code
      // path me.mioclient.nick.Settings.initialize now runs during construction). Anything the module's
      // constructor already populated is skipped there, so this is a no-op for full modules
      // and a repair for shell modules whose constructor threw before initializing settings.
      MioSettings.populate(m, unsafe);
      Class<?> klass = m.getClass();
      while (klass != null && klass != Object.class) {
         for (java.lang.reflect.Field f : klass.getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isStatic(f.getModifiers())) continue;
            if (me.mioclient.setting.Setting.class.isAssignableFrom(f.getType())) continue;
            try {
               f.setAccessible(true);
               Object cur = f.get(m);
               if (cur != null) continue;
               Object val = null;
               {
                  // Pick a default impl for common collection / Map / Set / Queue / Deque interfaces so
                  // shell modules don't keep null List/Map fields that NPE the first time a callback fires.
                  Class<?> ftype = f.getType();
                  if (ftype.isInterface()) {
                     if (ftype == java.util.List.class || ftype == java.util.Collection.class || ftype == Iterable.class) val = new java.util.ArrayList<>();
                     else if (ftype == java.util.Queue.class || ftype == java.util.Deque.class) val = new java.util.ArrayDeque<>();
                     else if (ftype == java.util.Set.class) val = new java.util.LinkedHashSet<>();
                     else if (ftype == java.util.Map.class) val = new java.util.LinkedHashMap<>();
                     else if (ftype == java.util.NavigableSet.class || ftype == java.util.SortedSet.class) val = new java.util.TreeSet<>();
                     else if (ftype == java.util.NavigableMap.class || ftype == java.util.SortedMap.class) val = new java.util.TreeMap<>();
                  }
                  if (val != null) {
                     // fall through to assignment below
                  } else {
                  String tn = ftype.getName();
                  boolean mio = tn.startsWith("me.mioclient.");
                  boolean atomicOrCollection =
                     tn.startsWith("java.util.concurrent.atomic.")
                     || tn.equals("java.util.ArrayList")
                     || tn.equals("java.util.LinkedList")
                     || tn.equals("java.util.HashMap")
                     || tn.equals("java.util.LinkedHashMap")
                     || tn.equals("java.util.HashSet")
                     || tn.equals("java.util.LinkedHashSet")
                     || tn.startsWith("it.unimi.dsi.fastutil.");
                  boolean concrete = !ftype.isInterface() && !java.lang.reflect.Modifier.isAbstract(ftype.getModifiers());
                  if (concrete && (mio || atomicOrCollection)) {
                     try {
                        java.lang.reflect.Constructor<?> c = f.getType().getDeclaredConstructor();
                        c.setAccessible(true);
                        val = c.newInstance();
                     } catch (Throwable noNoArg) {
                        if (mio) {
                           // try a ctor that takes the owning module type (Class_0104(ESPModule), …)
                           for (java.lang.reflect.Constructor<?> c : f.getType().getDeclaredConstructors()) {
                              Class<?>[] params = c.getParameterTypes();
                              if (params.length == 1 && params[0].isInstance(m)) {
                                 try {
                                    c.setAccessible(true);
                                    val = c.newInstance(m);
                                    break;
                                 } catch (Throwable ignore) {}
                              }
                           }
                        }
                     }
                  }
                  }
               }
               if (val == null) continue;
               // bypass final field restriction via Unsafe
               if (java.lang.reflect.Modifier.isFinal(f.getModifiers()) && unsafe != null) {
                  unsafe.putObject(m, unsafe.objectFieldOffset(f), val);
               } else {
                  f.set(m, val);
               }
            } catch (Throwable t) {
               // ignore: leave null
            }
         }
         klass = klass.getSuperclass();
      }
      // Original ctors register settings in declaration order; superclass fields end up first.
      // Match that order so the click-GUI shows things grouped sensibly. (For shell modules
      // whose constructor never ran me.mioclient.nick.Settings.initialize, this is the only place it happens.)
      MioSettings.orderRegistry(m);
   }

   private MioModules() { super(); }
}
