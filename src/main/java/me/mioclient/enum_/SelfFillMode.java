package me.mioclient.enum_;

import java.util.Collection;
import me.mioclient.api.Nameable;
import me.mioclient.setting.Setting;

/** SelfFill targeting: any or whitelist. */
public enum SelfFillMode implements Nameable {
   ANY("Any"),
   WHITELIST("WhiteList") {
      @Override
      public <T> boolean method_2(T var1, Collection<T> var2) {
         return var2.contains(var1);
      }
   },
   BLACKLIST("BlackList") {
      @Override
      public <T> boolean method_2(T var1, Collection<T> var2) {
         return !WHITELIST.method_2(var1, var2);
      }
   };

   public final String displayName;

    SelfFillMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public <T> boolean method_2(T var1, Collection<T> var2) {
      return true;
   }

   public <T> boolean method_2(T var1, Setting<? extends Collection<T>> var2) {
      return this.method_2(var1, (Collection<Object>)var2.getValue());
   }
}
