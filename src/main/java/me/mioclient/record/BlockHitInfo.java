package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

/** A block hit: the face Direction, the BlockPos, and the precise Vec3d hit point. */
public final class BlockHitInfo {
   public final Direction direction;
   public final BlockPos pos;
   public final Vec3 hitVec;

   public BlockHitInfo(Direction var1, BlockPos var2, Vec3 var3) {
      super();
      this.direction = var1;
      this.pos = var2;
      this.hitVec = var3;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Vec3 method_564() {
      return this.hitVec;
   }
}
