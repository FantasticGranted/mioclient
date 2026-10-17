package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** A BlockPos, the Direction of a face, and that face's Box, with a center-offset helper. */
public final class BlockFaceBox {
   public final BlockPos pos;
   public final Direction direction;
   public final AABB box;

   public BlockFaceBox(BlockPos var1, Direction var2, AABB var3) {
      super();
      this.pos = var1;
      this.direction = var2;
      this.box = var3;
   }

   public Vec3 getOffsetCenter() {
      return this.pos.getCenter().relative(this.direction, 0.5);
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public AABB getBox() {
      return this.box;
   }
}
