package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired when an entity is removed from the world. */
public class EntityRemoveEvent extends Event {
   public final int field_4313;

   public EntityRemoveEvent(int var1) {
      super();
      this.field_4313 = var1;
   }

   public int getId() {
      return this.field_4313;
   }
}
