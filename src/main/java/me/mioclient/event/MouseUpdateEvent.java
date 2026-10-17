package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired while updating mouse look. */
public class MouseUpdateEvent extends Event {
   public final double field_2375;
   public final double field_2376;

   public MouseUpdateEvent(double var1, double var3) {
      super();
      this.field_2375 = var1;
      this.field_2376 = var3;
   }

   public double method_712() {
      return this.field_2375;
   }

   public double method_713() {
      return this.field_2376;
   }
}
