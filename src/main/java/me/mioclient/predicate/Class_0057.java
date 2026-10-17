package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.EspMode;
import me.mioclient.module.render.ESPModule;

public class Class_0057 implements Predicate {
   public ESPModule eSP;

   public Class_0057(ESPModule var1) {
      super();
      this.eSP = var1;
   }

   @Override
   public boolean test(Object var1) {
      return (this.eSP.mode.getValue() == EspMode.TEXT || this.eSP.mode.getValue() == EspMode.BOTH)
         && this.eSP.targets.method_194()
         && this.eSP.items.method_194();
   }
}
