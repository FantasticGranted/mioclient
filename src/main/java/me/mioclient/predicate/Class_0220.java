package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.InstantMode;
import me.mioclient.module.combat.AutoCrystalModule;

public class Class_0220 implements Predicate {
   public AutoCrystalModule autoCrystal;

   public Class_0220(AutoCrystalModule var1) {
      super();
      this.autoCrystal = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoCrystal.instant.getValue() != InstantMode.NONE;
   }
}
