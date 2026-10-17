package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.EspMode;
import me.mioclient.module.render.ESPModule;

public class Class_0164 implements Predicate {
   public ESPModule eSP;

   public Class_0164(ESPModule var1) {
      super();
      this.eSP = var1;
   }

   @Override
   public boolean test(Object var1) {
      return (this.eSP.mode.getValue() == EspMode.BOX || this.eSP.mode.getValue() == EspMode.BOTH)
         && this.eSP.targets.method_194()
         && this.eSP.items.method_194();
   }
}
