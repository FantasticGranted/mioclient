package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.phys.BlockHitResult;

/** Fired when the player interacts with a block. */
public class InteractBlockEvent extends Event {
   public final BlockHitResult field_1098;
   public final InteractionHand field_1099;

   public InteractBlockEvent(BlockHitResult var1, InteractionHand var2) {
      super();
      this.field_1098 = var1;
      this.field_1099 = var2;
   }

   public BlockHitResult method_382() {
      return this.field_1098;
   }

   public InteractionHand method_12() {
      return this.field_1099;
   }
}
