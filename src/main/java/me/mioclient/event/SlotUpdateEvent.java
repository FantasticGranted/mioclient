package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired when the selected hotbar slot changes. */
public class SlotUpdateEvent extends Event {
   public final int field_172;

   public SlotUpdateEvent(int var1) {
      super();
      this.field_172 = var1;
   }

   public int method_34() {
      return this.field_172;
   }
}
