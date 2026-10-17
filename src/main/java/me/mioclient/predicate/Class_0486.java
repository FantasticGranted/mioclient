package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.AuraModule;

public class Class_0486 implements Predicate {
   public AuraModule aura;

   public Class_0486(AuraModule var1) {
      super();
      this.aura = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.aura.targets.method_194() && this.aura.players.method_194();
   }
}
