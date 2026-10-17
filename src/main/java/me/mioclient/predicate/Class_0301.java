package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.AntiAimYaw;
import me.mioclient.module.misc.AntiAimModule;

public class Class_0301 implements Predicate {
   public AntiAimModule antiAim;

   public Class_0301(AntiAimModule var1) {
      super();
      this.antiAim = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.antiAim.yaw2.getValue() == AntiAimYaw.FLIP || this.antiAim.yaw2.getValue() == AntiAimYaw.JITTER;
   }
}
