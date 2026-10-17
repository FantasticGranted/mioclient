package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/** A BlockPos paired with a Direction (e.g. a placement side). */
public final class DirectionalBlockPos {
   public final Direction direction;
   public final BlockPos pos;

   public DirectionalBlockPos(Direction var1, BlockPos var2) {
      super();
      this.direction = var1;
      this.pos = var2;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public BlockPos getPos() {
      return this.pos;
   }
}
