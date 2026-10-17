package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.world.InteractionHand;

/** Fired when the player uses an item in hand. */
public class UseItemEvent extends Event {
   public final InteractionHand field_19;

   public UseItemEvent(InteractionHand var1) {
      super();
      this.field_19 = var1;
   }

   public InteractionHand method_12() {
      return this.field_19;
   }
}
