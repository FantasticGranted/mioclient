package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.MoveEvent;
import net.minecraft.client.player.ClientInput;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Movement math: input-and-yaw to horizontal velocity, effective speed with speed/slowness
 * effects, jump-boost, auto-step height, strafe yaw and velocity-direction tests.
 */
public class MovementUtil implements MinecraftAccess {
   public static float field_1470 = 0.2873F;
   public static float field_1471 = field_1470 * 0.2F;

   public MovementUtil() {
      super();
   }

   public static double[] method_2(MoveEvent var0, double var1) {
      if (method_363()) {
         double[] var3 = method_2(mc.player.getViewYRot(RenderUtil.method_776()), mc.player.input, var1);
         var0.method_7(var3[0], var3[1]);
         return var3;
      } else {
         var0.method_7(0.0, 0.0);
         return new double[]{0.0, 0.0};
      }
   }

   public static double[] method_2(float var0, ClientInput var1, double var2) {
      float var4 = var1.moveVector.y;
      float var5 = var1.moveVector.x;
      float var6 = var0;
      if (var4 != 0.0F) {
         if (var5 > 0.0F) {
            var6 = var0 + (float)(var4 > 0.0F ? -Class_0245.field_684 : Class_0245.field_684);
         } else if (var5 < 0.0F) {
            var6 = var0 + (float)(var4 > 0.0F ? Class_0245.field_684 : -Class_0245.field_684);
         }

         var5 = 0.0F;
         if (var4 > 0.0F) {
            var4 = 1F;
         } else if (var4 < 0.0F) {
            var4 = -1F;
         }
      }

      double var7 = (double)var4 * var2 * -Math.sin(Math.toRadians((double)var6)) + (double)var5 * var2 * Math.cos(Math.toRadians((double)var6));
      double var9 = (double)var4 * var2 * Math.cos(Math.toRadians((double)var6)) - (double)var5 * var2 * -Math.sin(Math.toRadians((double)var6));
      return new double[]{var7, var9};
   }

   public static double method_2(double var0, double var2, double var4, long var6) {
      if (var2 >= var0) {
         return var0;
      } else {
         double var8 = Mth.clamp(
            (double)(System.currentTimeMillis() - var6) / (var4 * 1e+03),
            0.0,
            1.0
         );
         double var10 = var2 + (var0 - var2) * var8;
         return Math.min(var10, var0);
      }
   }

   public static boolean method_9(Player var0) {
      return var0 == null ? false : var0.zza != 0.0F || var0.xxa != 0.0F;
   }

   public static boolean method_363() {
      return method_9(mc.player);
   }

   public static double method_78(boolean var0) {
      return method_2(var0, (double)field_1470);
   }

   public static double method_2(boolean var0, double var1) {
      double var3 = var1;
      if (mc.player.hasEffect(MobEffects.SPEED)) {
         int var5 = mc.player.getEffect(MobEffects.SPEED).getAmplifier();
         var3 = var1 * (1.0 + 0.2 * (double)(var5 + 1));
      }

      if (var0 && mc.player.hasEffect(MobEffects.SLOWNESS)) {
         int var6 = mc.player.getEffect(MobEffects.SLOWNESS).getAmplifier();
         var3 /= 1.0 + 0.2 * (double)(var6 + 1);
      }

      return var3;
   }

   public static double method_495() {
      double var0 = 0.0;
      if (mc.player.hasEffect(MobEffects.JUMP_BOOST)) {
         int var2 = mc.player.getEffect(MobEffects.JUMP_BOOST).getAmplifier();
         var0 += (double)(var2 + 1) * 0.1;
      }

      return var0;
   }

   public static double method_9(double var0) {
      boolean var2 = mc.player.onGround() && mc.player.horizontalCollision;
      if (!var2) {
         return 0.0;
      } else {
         double var3 = -1.0;
         AABB var5 = mc.player
            .getBoundingBox()
            .move(0.0, 0.05, 0.0)
            .inflate(0.05);
         var5 = var5.setMaxY(var5.maxY + var0);

         for (VoxelShape var7 : mc.level.getCollisions(mc.player, var5)) {
            for (AABB var9 : var7.toAabbs()) {
               if (var9.maxY > var3) {
                  var3 = var9.maxY;
               }
            }
         }

         var3 -= mc.player.getY();
         return var3 > 0.0 && var3 <= var0 ? var3 : 0.0;
      }
   }

   public static boolean method_2(Player var0, Vec3 var1) {
      Vec3 var2 = var0.getDeltaMovement();
      if (method_9(var0)) {
         Vec3 var3 = var0.position()
            .add(var2.x() * 1e+04, 0.0, var2.z() * 1e+04);
         Vec3 var4 = var0.position()
            .add(var2.x() * -1e+04, 0.0, var2.z() * -1e+04);
         return var3.distanceToSqr(var1) < var4.distanceToSqr(var1);
      } else {
         return false;
      }
   }

   public static boolean method_2(Vec3 var0) {
      return method_2(mc.player, var0);
   }

   public static float method_496() {
      if (mc.player.isFallFlying()) {
         double[] var3 = method_2(mc.player.getYRot(), mc.player.input, 1.0);
         return (float)(Math.toDegrees(Math.atan2(var3[1], var3[0])) - (double)Class_0245.field_685);
      } else {
         float var0 = mc.player.getYRot();
         ClientInput var1 = mc.player.input;
         boolean var2 = var1.keyPresses.forward() != var1.keyPresses.backward();
         if (var1.keyPresses.backward()) {
            var0 += 1.8e+02F;
         }

         if (var1.keyPresses.right() && !var2) {
            var0 += (float)(Class_0245.field_685 * (var1.keyPresses.backward() ? -1 : 1));
         }

         if (var1.keyPresses.left() && !var2) {
            var0 -= (float)(Class_0245.field_685 * (var1.keyPresses.backward() ? -1 : 1));
         }

         return var0;
      }
   }
}
