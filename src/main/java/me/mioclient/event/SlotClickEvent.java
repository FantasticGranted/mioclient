package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.world.inventory.Slot;

/** Fired when an inventory slot is clicked. */
public class SlotClickEvent extends Event {
   public final Slot field_3478;

   public SlotClickEvent(Slot var1) {
      super();
      this.field_3478 = var1;
   }

   public Slot method_1006() {
      return this.field_3478;
   }
}
