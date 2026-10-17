package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;

/** A candidate end-crystal placement: place/target positions, expected target and self damage, and the block hit result. */
public final class CrystalPlacement {
   public final BlockPos placePos;
   public final BlockPos targetPos;
   public final float damage;
   public final float selfDamage;
   public final BlockHitResult hitResult;

   public CrystalPlacement(BlockPos var1, BlockPos var2, float var3, float var4, BlockHitResult var5) {
      super();
      this.placePos = var1;
      this.targetPos = var2;
      this.damage = var3;
      this.selfDamage = var4;
      this.hitResult = var5;
   }

   public BlockPos getPlacePos() {
      return this.placePos;
   }

   public BlockPos getTargetPos() {
      return this.targetPos;
   }

   public float getDamage() {
      return this.damage;
   }

   public float getSelfDamage() {
      return this.selfDamage;
   }

   public BlockHitResult getHitResult() {
      return this.hitResult;
   }
}
