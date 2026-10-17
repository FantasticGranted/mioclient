package me.mioclient.api;

import net.minecraft.core.BlockPos;

/** Mixin accessor for the current block-breaking state and progress. */
public interface BlockBreakingAccess {
   boolean isBreakingBlock();

   void setBreakingBlock(boolean var1);

   float getBreakingProgress();

   void setBreakingProgress(float var1);

   BlockPos getCurrentBreakingBlock();

   void setCurrentBreakingBlock(BlockPos var1);
}
