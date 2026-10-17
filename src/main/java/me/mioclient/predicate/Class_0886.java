package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.exploit.FastProjectileModule;

public class Class_0886 implements Predicate {
   public FastProjectileModule fastProjectile;

   public Class_0886(FastProjectileModule var1) {
      super();
      this.fastProjectile = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.fastProjectile.whitelist.method_194();
   }
}
