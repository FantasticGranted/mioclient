package me.mioclient.record;

import net.minecraft.world.phys.Vec3;

/** A recorded player movement frame (position, yaw, pitch, head yaw, velocity, sneaking, on-ground) for replay. */
public final class PlayerFrame {
   public final Vec3 field_806;
   public final float field_1760;
   public final float field_1761;
   public final float field_2836;
   public final Vec3 field_2837;
   public final boolean field_2838;
   public final boolean field_2839;

   public PlayerFrame(Vec3 var1, float var2, float var3, float var4, Vec3 var5, boolean var6, boolean var7) {
      super();
      this.field_806 = var1;
      this.field_1760 = var2;
      this.field_1761 = var3;
      this.field_2836 = var4;
      this.field_2837 = var5;
      this.field_2838 = var6;
      this.field_2839 = var7;
   }

   public Vec3 getPos() {
      return this.field_806;
   }

   public float getYaw() {
      return this.field_1760;
   }

   public float getPitch() {
      return this.field_1761;
   }

   public float getHeadYaw() {
      return this.field_2836;
   }

   public Vec3 getVelocity() {
      return this.field_2837;
   }

   public boolean isSneaking() {
      return this.field_2838;
   }

   public boolean isOnGround() {
      return this.field_2839;
   }
}
