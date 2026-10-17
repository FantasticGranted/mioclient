package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired when the server updates the selected hotbar slot. */
public final class ServerSlotUpdateEvent extends Event {
   public final int field_1849;

   public ServerSlotUpdateEvent(int var1) {
      super();
      this.field_1849 = var1;
   }

   public int method_34() {
      return this.field_1849;
   }
}
