package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** Immutable pairing of a BlockPos with the BlockState recorded at that position. */
public final class PositionedBlockState {
   public final BlockPos pos;
   public final BlockState state;

   public PositionedBlockState(BlockPos var1, BlockState var2) {
      super();
      this.pos = var1;
      this.state = var2;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public BlockState getState() {
      return this.state;
   }
}
