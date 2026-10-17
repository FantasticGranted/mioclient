package me.mioclient.api;

import net.minecraft.world.damagesource.DamageSource;

/** Mixin hook to invoke the superclass damage logic. */
public interface SuperDamageAccess {
   default boolean damageSuper(DamageSource var1, float var2) {
      return true;
   }
}
