package me.mioclient.event;

import me.mioclient.enum_.PreType;
import me.mioclient.internal.Event;

/** Fired around a render phase (carries pre/post type). */
public class RenderEvent extends Event {
   public final PreType field_1857;

   public RenderEvent(PreType var1) {
      super();
      this.field_1857 = var1;
   }

   public PreType method_213() {
      return this.field_1857;
   }
}
