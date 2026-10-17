package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.world.phys.Vec3;

/** Fired while updating the camera position. */
public class CameraEvent extends Event {
   public double field_608;
   public double field_609;
   public double field_2311;

   public CameraEvent(double var1, double var3, double var5) {
      super();
      this.field_608 = var1;
      this.field_609 = var3;
      this.field_2311 = var5;
   }

   public void method_38(Vec3 var1) {
      this.field_608 = var1.x;
      this.field_609 = var1.y;
      this.field_2311 = var1.z;
   }

   public Vec3 method_733() {
      return new Vec3(this.field_608, this.field_609, this.field_2311);
   }
}
