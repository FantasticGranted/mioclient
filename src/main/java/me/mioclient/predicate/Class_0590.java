package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.SwapMode;
import me.mioclient.module.combat.AuraModule;

public class Class_0590 implements Predicate {
   public AuraModule aura;

   public Class_0590(AuraModule var1) {
      super();
      this.aura = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.aura.swap.getValue() == SwapMode.SILENT;
   }
}
