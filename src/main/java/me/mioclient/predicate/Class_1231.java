package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.SwapMode;
import me.mioclient.enum_.WeaponMode;
import me.mioclient.module.combat.AuraModule;

public class Class_1231 implements Predicate {
   public AuraModule aura;

   public Class_1231(AuraModule var1) {
      super();
      this.aura = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.aura.swap.getValue() != SwapMode.SILENT || this.aura.weapon.getValue() != WeaponMode.SWAP;
   }
}
