package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.core.BlockPos;

/** Fired when the player breaks a block (carries the position). */
public class BlockBreakEvent extends Event {
   public BlockPos field_355;

   public BlockBreakEvent(BlockPos var1) {
      super();
      this.field_355 = var1;
   }

   public BlockPos method_111() {
      return this.field_355;
   }
}
