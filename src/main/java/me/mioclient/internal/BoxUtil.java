package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Bounding-box math: centers/top/bottom points, shrink/expand, corner sampling sorted by
 * distance, unit-box tests, and server-side hitbox reconstruction.
 */
public class BoxUtil implements MinecraftAccess {
   public static double field_2280 = 0.0625;

   public BoxUtil() {
      super();
   }

   public static Vec3 method_2(AABB var0) {
      return new Vec3(Mth.lerp(Class_0245.field_688, var0.minX, var0.maxX), var0.minY, Mth.lerp(Class_0245.field_688, var0.minZ, var0.maxZ));
   }

   public static Vec3 method_9(AABB var0) {
      return method_2(var0).add(0.0, var0.getYsize() * 0.25, 0.0);
   }

   public static Vec3 method_5(AABB var0) {
      return new Vec3(Mth.lerp(Class_0245.field_688, var0.minX, var0.maxX), var0.maxY, Mth.lerp(Class_0245.field_688, var0.minZ, var0.maxZ));
   }

   public static AABB method_2(AABB var0, Vec3 var1) {
      double var2 = var0.getXsize() * Class_0245.field_688;
      double var4 = var0.getYsize() * Class_0245.field_688;
      double var6 = var0.getZsize() * Class_0245.field_688;
      return new AABB(var1.x - var2, var1.y - var4, var1.z - var6, var1.x + var2, var1.y + var4, var1.z + var6);
   }

   public static AABB method_2(AABB var0, BlockPos var1) {
      return method_2(
         var0,
         new Vec3(
            (double)var1.getX() + Class_0245.field_688,
            (double)var1.getY() + var0.getYsize() * Class_0245.field_688,
            (double)var1.getZ() + Class_0245.field_688
         )
      );
   }

   public static AABB method_34(BlockPos var0) {
      BlockState var1 = mc.level.getBlockState(var0);
      if (!var1.isAir() && !var1.is(Blocks.AIR)) {
         VoxelShape var2 = var1.getShape(mc.level, var0);
         if (var2.isEmpty()) {
            var2 = Shapes.box(
               0.0,
               0.0,
               0.0,
               1.0,
               1.0,
               1.0
            );
         }

         return var2.bounds().move(var0);
      } else {
         return new AABB(var0);
      }
   }

   public static List<Vec3> method_2(AABB var0, double var1) {
      ArrayList<Vec3> var3 = new ArrayList<>(
         List.of(
            new Vec3(var0.minX, var1, var0.minZ),
            new Vec3(var0.maxX, var1, var0.minZ),
            new Vec3(var0.minX, var1, var0.maxZ),
            new Vec3(var0.maxX, var1, var0.maxZ)
         )
      );
      var3.sort(Comparator.comparing((Vec3 var0x) -> mc.player.getEyePosition().distanceToSqr(var0x)));
      return var3;
   }

   public static boolean method_7(AABB var0) {
      return var0.getZsize() == 1.0
         && var0.getYsize() == 1.0
         && var0.getXsize() == 1.0;
   }

   public static boolean method_2(VoxelShape var0) {
      return var0.isEmpty() ? false : method_7(var0.bounds());
   }

   public static int method_2(Player var0, Setting<Integer> var1) {
      if (var1.method_468()) {
         double var2 = Math.hypot(var0.getX() - var0.xo, var0.getZ() - var0.zo)
            * 2e+01
            * 3.6;
         if (var2 < 3.0) {
            return 0;
         } else if (var2 <= 1e+01) {
            return 1;
         } else if (var2 <= 2e+01) {
            return 3;
         } else {
            return var2 <= 3e+01 ? 4 : 5;
         }
      } else {
         return (Integer)var1.getValue();
      }
   }

   public static Vec3 method_9(Vec3 var0, AABB var1) {
      return new Vec3(
         Mth.clamp(var0.x(), var1.minX, var1.maxX),
         Mth.clamp(var0.y(), var1.minY, var1.maxY),
         Mth.clamp(var0.z(), var1.minZ, var1.maxZ)
      );
   }

   public static AABB method_2(Entity var0, float var1) {
      double var2 = Mth.lerp((double)var1, var0.xOld, var0.getX()) - var0.getX();
      double var4 = Mth.lerp((double)var1, var0.yOld, var0.getY()) - var0.getY();
      double var6 = Mth.lerp((double)var1, var0.zOld, var0.getZ()) - var0.getZ();
      AABB var8 = var0.getBoundingBox();
      return var8.move(var2, var4, var6);
   }

   public static AABB method_4(LivingEntity var0) {
      if (var0 instanceof FakePlayerEntity) {
         return var0.getBoundingBox();
      } else {
         AABB var1 = var0.getBoundingBox();
         Vec3 var3 = var0.getInterpolation().position();
         if (var3.lengthSqr() == 0.0) {
            return var0.getBoundingBox();
         } else {
            double var4 = var1.getXsize() / 2.0;
            double var6 = var1.getZsize() / 2.0;
            var1 = new AABB(-var4, 0.0, -var6, var4, var1.getYsize(), var6);
            return var1.move(var3);
         }
      }
   }

   public static boolean method_2(AABB var0, AABB var1) {
      AABB var2 = var0.intersect(var1);
      float var3 = (float)Math.min(var2.getXsize(), Math.min(var2.getYsize(), var2.getZsize()));
      return (double)var3 > 1e-07;
   }
}
