package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired on mouse scroll. */
public class MouseScrollEvent extends Event {
   public final double field_1882;
   public final double field_1883;

   public MouseScrollEvent(double var1, double var3) {
      super();
      this.field_1882 = var1;
      this.field_1883 = var3;
   }

   public double method_617() {
      return this.field_1882;
   }

   public double method_618() {
      return this.field_1883;
   }
}
