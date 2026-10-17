package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.EspMode;
import me.mioclient.module.render.ESPModule;

public class Class_1175 implements Predicate {
   public ESPModule eSP;

   public Class_1175(ESPModule var1) {
      super();
      this.eSP = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.eSP.mode.getValue() == EspMode.TEXT || this.eSP.mode.getValue() == EspMode.BOTH;
   }
}
