package me.mioclient.internal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

/**
 * General math helpers: floor-rounding to decimals, random ranges, lerp, degree/radian
 * conversion, direction vectors, distance-based render scaling, angle diff and wrap.
 */
public class MathUtil implements MinecraftAccess {
   public MathUtil() {
      super();
   }

   public static double method_2(double var0, int var2) {
      if (var2 < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal var3 = BigDecimal.valueOf(var0);
         var3 = var3.setScale(var2, RoundingMode.FLOOR);
         return var3.doubleValue();
      }
   }

   public static float method_2(float var0, int var1) {
      if (var1 < 0) {
         throw new IllegalArgumentException();
      } else {
         BigDecimal var2 = BigDecimal.valueOf((double)var0);
         var2 = var2.setScale(var1, RoundingMode.FLOOR);
         return var2.floatValue();
      }
   }

   public static float method_2(float var0, float var1) {
      return var0 + new Random().nextFloat() * (var1 - var0);
   }

   public static int method_7(int var0, int var1) {
      return var0 + new Random().nextInt() * (var1 - var0);
   }

   public static double method_2(double var0, double var2, double var4) {
      return var0 + (var2 - var0) * var4;
   }

   public static float method_7(float var0) {
      return var0 * Class_0245.field_690;
   }

   public static float method_29(float var0) {
      return var0 * Class_0245.field_691;
   }

   public static Vec3 method_9(float var0, float var1) {
      float var2 = method_7(var0);
      float var3 = method_7(-var1);
      float var4 = Mth.cos(var3);
      float var5 = Mth.sin(var3);
      float var6 = Mth.cos(var2);
      float var7 = Mth.sin(var2);
      return new Vec3((double)(var5 * var6), (double)(-var7), (double)(var4 * var6));
   }

   public static double method_2(Vec3 var0, double var1) {
      return method_2(mc.gameRenderer.getMainCamera().position(), var0, var1);
   }

   public static double method_2(Vec3 var0, Vec3 var1, double var2) {
      double var4 = var0.distanceTo(var1);
      double var6 = (double)((Integer)mc.options.fov().get()).intValue() / 1.1e+02;
      if (var6 < 1.0) {
         var6 = 1.0;
      }

      return var4 <= 1e+01 / var2
         ? Math.min(1.0, var2) * var6
         : var4 * var2 * 0.1 * var6;
   }

   public static float method_38(Player var0) {
      float[] var1 = RotationManager.method_78(var0.position());
      return Mth.degreesDifferenceAbs(mc.player.getYRot(), var1[0]);
   }

   public static double method_9(double var0, double var2, double var4) {
      double var6 = var0 - var2;
      double var8 = var4 - var0;
      return var8 > var6 ? var2 : var4;
   }

   public static boolean method_17(int var0) {
      return var0 == 0 ? false : new Random().nextInt(100) <= var0;
   }

   public static float method_4(float var0) {
      return (var0 < 0.0F ? var0 + 3.6e+02F : var0) % 3.6e+02F;
   }

   public static float method_5(float var0, float var1) {
      return Mth.lerp(RenderUtil.method_776(), var0, var1);
   }

   public static double method_2(double var0, double var2) {
      return Mth.lerp((double)RenderUtil.method_776(), var0, var2);
   }

   public static double method_35(BlockPos var0) {
      return method_7(var0.getCenter());
   }

   public static double method_7(Vec3 var0) {
      return mc.gameRenderer.getMainCamera().position().distanceTo(var0);
   }
}
