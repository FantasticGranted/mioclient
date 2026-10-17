package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.exploit.FastProjectileModule;

public class Class_0815 implements Predicate {
   public FastProjectileModule fastProjectile;

   public Class_0815(FastProjectileModule var1) {
      super();
      this.fastProjectile = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.fastProjectile.whitelist.method_194();
   }
}
