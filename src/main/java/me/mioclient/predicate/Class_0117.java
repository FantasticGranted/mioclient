package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.AutoSwapMode;
import me.mioclient.module.combat.AutoCrystalModule;

public class Class_0117 implements Predicate {
   public AutoCrystalModule autoCrystal;

   public Class_0117(AutoCrystalModule var1) {
      super();
      this.autoCrystal = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoCrystal.autoSwap.getValue() == AutoSwapMode.NORMAL;
   }
}
