package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired on a mouse button action. */
public final class MouseButtonEvent extends Event {
   public final int field_1877;
   public final boolean field_1878;

   public MouseButtonEvent(int var1, boolean var2) {
      super();
      this.field_1877 = var1;
      this.field_1878 = var2;
   }

   public int method_614() {
      return this.field_1877;
   }

   public boolean method_615() {
      return this.field_1878;
   }
}
