package me.mioclient.internal;

import com.google.gson.JsonElement;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import me.mioclient.setting.CustomSetting2;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

/** Multi-select setting whose candidate values come from a Minecraft {@code Registry<T>}. */
public class RegistrySetting<T> extends CustomSetting2<T> {
   public final Registry<T> field_555;

   @SafeVarargs
   public RegistrySetting(String var1, Registry<T> var2, T... var3) {
      super(var1, (T[])var3);
      this.field_555 = var2;
   }

   @SafeVarargs
   public RegistrySetting(String var1, Registry<T> var2, Predicate<Set<T>> var3, T... var4) {
      super(var1, var3, (T[])var4);
      this.field_555 = var2;
   }

   @Override
   public T method_38(String var1) {
      return (T)this.field_555.getOptional(Identifier.parse(var1)).orElse(null);
   }

   @Override
   public String method_9(T var1) {
      Identifier var2 = this.field_555.getKey(var1);
      return var2 == null ? null : var2.toShortLanguageKey();
   }

   @Override
   public Collection<T> method_230() {
      return this.field_555.stream().toList();
   }

   @Override
   public void fromJson(JsonElement var1) {
      if (var1.isJsonArray()) {
         ((Set)this.getValue()).clear();

         for (JsonElement var4 : var1.getAsJsonArray()) {
            Object var5 = this.field_555.get(Identifier.parse(var4.getAsString()));
            ((Set)this.getValue()).add(var5);
         }
      }
   }

   public Registry<T> method_231() {
      return this.field_555;
   }
}
