package me.mioclient.record;

import net.minecraft.core.BlockPos;

/** A BlockPos tagged with a long timestamp (e.g. when it was recorded/last seen). */
public final class TimestampedBlockPos {
   public final BlockPos pos;
   public final long time;

   public TimestampedBlockPos(BlockPos var1, long var2) {
      super();
      this.pos = var1;
      this.time = var2;
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public long method_417() {
      return this.time;
   }
}
