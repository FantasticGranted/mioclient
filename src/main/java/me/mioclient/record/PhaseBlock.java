package me.mioclient.record;

import me.mioclient.enum_.BlockSafety;
import net.minecraft.core.BlockPos;

public final class PhaseBlock {
   public final BlockPos pos;
   public final BlockSafety safety;

   public PhaseBlock(BlockPos var1, BlockSafety var2) {
      super();
      this.pos = var1;
      this.safety = var2;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public BlockSafety getSafety() {
      return this.safety;
   }
}
