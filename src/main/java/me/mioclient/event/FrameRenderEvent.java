package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired during frame render (flags whether the world was drawn). */
public class FrameRenderEvent extends Event {
   public final boolean field_80;

   public FrameRenderEvent(boolean var1) {
      super();
      this.field_80 = var1;
   }

   public boolean method_93() {
      return this.field_80;
   }
}
