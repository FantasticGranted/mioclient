package me.mioclient.internal;

import net.minecraft.world.phys.Vec3;

/** A position stamped with its creation time. */
public class TimedPosition {
   public final long field_1103 = System.currentTimeMillis();
   public final Vec3 field_1104;

   public TimedPosition(Vec3 var1) {
      super();
      this.field_1104 = var1;
   }

   public long method_383() {
      return this.field_1103;
   }

   public Vec3 method_384() {
      return this.field_1104;
   }
}
