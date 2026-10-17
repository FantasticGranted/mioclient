package me.mioclient.record;

import me.mioclient.api.MinecraftAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/** A snapshot of the BlockState at a BlockPos with a capture timestamp; expires after 500 ms. */
public final class TimedBlockState implements MinecraftAccess {
   public final BlockState state;
   public final BlockPos pos;
   public final long time;

   public TimedBlockState(BlockState var1, BlockPos var2, long var3) {
      super();
      this.state = var1;
      this.pos = var2;
      this.time = var3;
   }

   public static TimedBlockState of(BlockPos var0) {
      return new TimedBlockState(mc.level.getBlockState(var0), var0, System.currentTimeMillis());
   }

   public boolean method_644() {
      return System.currentTimeMillis() - this.time >= 500L;
   }

   public BlockState getState() {
      return this.state;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public long getTime() {
      return this.time;
   }
}
