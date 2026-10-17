package me.mioclient.enum_;

import java.util.function.BiFunction;
import me.mioclient.api.Nameable;
import me.mioclient.internal.Class_0084;
import me.mioclient.internal.Class_0179;
import me.mioclient.internal.CommandBase;
import me.mioclient.internal.Class_0298;
import me.mioclient.internal.Class_0678;
import me.mioclient.record.Keybind;

public enum Class_1072 implements Nameable {
   SIMPLE("simple", Class_0298::new),
   QUEUE("queue", Class_0179::new),
   DOUBLE_TAP("double_tap", Class_0084::new),
   HOLD("hold", Class_0678::new);

   public final String field_3289;
   public final BiFunction<String, Keybind, CommandBase> field_3290;

    Class_1072(String var3, BiFunction<String, Keybind, CommandBase> var4) {
      this.field_3289 = var3;
      this.field_3290 = var4;
   }

   public static Class_1072 method_29(String var0) {
      for (Class_1072 var4 : values()) {
         if (var4.getName().equalsIgnoreCase(var0)) {
            return var4;
         }
      }

      return null;
   }

   @Override
   public String getName() {
      return this.field_3289;
   }

   public CommandBase method_2(String var1, Keybind var2) {
      return this.field_3290.apply(var1, var2);
   }
}
