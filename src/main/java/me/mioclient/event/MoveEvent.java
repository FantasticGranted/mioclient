package me.mioclient.event;

import me.mioclient.internal.Event;
import me.mioclient.internal.VectorUtil;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;

/** Fired on player movement (carries the movement vector and type). */
public class MoveEvent extends Event {
   public final Vec3 field_3852;
   public final Vec3 field_3853;
   public MoverType field_3854;

   public MoveEvent(Vec3 var1, MoverType var2) {
      super();
      this.field_3852 = new Vec3(var1.x, var1.y, var1.z);
      this.field_3853 = var1;
      this.field_3854 = var2;
   }

   public Vec3 method_1066() {
      return this.field_3853;
   }

   public void method_4(Vec3 var1) {
      VectorUtil.method_2(this.field_3853, var1.x, var1.y, var1.z);
   }

   public void method_7(double var1, double var3) {
      this.method_4(new Vec3(var1, this.method_395(), var3));
   }

   public double method_380() {
      return this.field_3853.x;
   }

   public double method_395() {
      return this.field_3853.y;
   }

   public void setY(double var1) {
      this.method_4(new Vec3(this.method_380(), var1, this.method_396()));
   }

   public double method_396() {
      return this.field_3853.z;
   }

   public MoverType method_1067() {
      return this.field_3854;
   }

   public void method_2(MoverType var1) {
      this.field_3854 = var1;
   }

   public Vec3 method_1068() {
      return this.field_3852;
   }
}
