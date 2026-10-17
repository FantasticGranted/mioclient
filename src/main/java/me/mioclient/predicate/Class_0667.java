package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.AuraModule;

public class Class_0667 implements Predicate {
   public AuraModule aura;

   public Class_0667(AuraModule var1) {
      super();
      this.aura = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.aura.render.method_194();
   }
}
