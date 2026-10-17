package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.MaceMode;
import me.mioclient.module.combat.AuraModule;

public class Class_0176 implements Predicate {
   public AuraModule aura;

   public Class_0176(AuraModule var1) {
      super();
      this.aura = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.aura.mace.method_176()
         && (this.aura.mace.getValue() == MaceMode.DENSITY || this.aura.mace.getValue() == MaceMode.SMART);
   }
}
