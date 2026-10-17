package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.AutoArmorModule;

public class Class_0778 implements Predicate {
   public AutoArmorModule autoArmor;

   public Class_0778(AutoArmorModule var1) {
      super();
      this.autoArmor = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoArmor.inRange.method_194();
   }
}
