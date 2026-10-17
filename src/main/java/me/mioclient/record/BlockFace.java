package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/** Immutable pairing of a BlockPos with a Direction (a specific face/side of a block). */
public final class BlockFace {
   public final BlockPos pos;
   public final Direction direction;

   public BlockFace(BlockPos var1, Direction var2) {
      super();
      this.pos = var1;
      this.direction = var2;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Direction getDirection() {
      return this.direction;
   }
}
