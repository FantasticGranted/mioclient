package me.mioclient.record;

import net.minecraft.core.BlockPos;

/** An ordered pair of BlockPos values (a start and an end position). */
public final class BlockPosPair {
   public final BlockPos start;
   public final BlockPos end;

   public BlockPosPair(BlockPos var1, BlockPos var2) {
      super();
      this.start = var1;
      this.end = var2;
   }

   public BlockPos getStart() {
      return this.start;
   }

   public BlockPos getEnd() {
      return this.end;
   }
}
