package me.mioclient.internal;

import me.mioclient.enum_.Class_1072;
import me.mioclient.record.Keybind;

public class Class_0298 extends CommandBase {
   public Class_0298(String var1, Keybind var2) {
      super(var1, Class_1072.SIMPLE, var2);
   }

   @Override
   public void run() {
      for (String var2 : this.field_789) {
         this.method_7(var2);
      }
   }
}
