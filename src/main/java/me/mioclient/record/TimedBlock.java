package me.mioclient.record;

import net.minecraft.world.level.block.Block;

/** A Block tagged with a timestamp (created via the millis factory) for time-based tracking. */
public final class TimedBlock {
   public final long time;
   public final Block block;

   public TimedBlock(long var1, Block var3) {
      super();
      this.time = var1;
      this.block = var3;
   }

   public static TimedBlock method_31(Block var0) {
      return new TimedBlock(System.currentTimeMillis(), var0);
   }

   public long method_417() {
      return this.time;
   }

   public Block getBlock() {
      return this.block;
   }
}
