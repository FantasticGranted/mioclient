package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.AntiAimPitch;
import me.mioclient.module.misc.AntiAimModule;

public class Class_0797 implements Predicate {
   public AntiAimModule antiAim;

   public Class_0797(AntiAimModule var1) {
      super();
      this.antiAim = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.antiAim.pitch2.getValue() == AntiAimPitch.STATIC;
   }
}
