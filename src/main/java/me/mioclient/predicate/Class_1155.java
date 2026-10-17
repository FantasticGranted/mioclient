package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.AutoSwapMode;
import me.mioclient.enum_.SilentMode;
import me.mioclient.module.combat.AutoCrystalModule;

public class Class_1155 implements Predicate {
   public AutoCrystalModule autoCrystal;

   public Class_1155(AutoCrystalModule var1) {
      super();
      this.autoCrystal = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoCrystal.autoSwap.getValue() == AutoSwapMode.SILENT && this.autoCrystal.silent.getValue() == SilentMode.ALTERNATIVE;
   }
}
