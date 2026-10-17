package me.mioclient.internal;

import java.util.List;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_0694;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Raycasting helpers: block-filtered raycasts (via FilteredRaycastContext), angle-based
 * rays, distance-to-ground/ceiling probes and line-of-sight visibility tests.
 */
public class RaycastUtil implements MinecraftAccess {
   public RaycastUtil() {
      super();
   }

   public static BlockHitResult method_2(FilteredRaycastContext var0) {
      return (BlockHitResult)BlockGetter.traverseBlocks(var0.method_543(), var0.method_544(), var0, (var1, var2) -> {
         BlockState var3 = mc.level.getBlockState(var2);
         Class_0694 var4 = var0.method_548().test(var2, var3);
         if (var4 == Class_0694.IGNORE) {
            var3 = Blocks.AIR.defaultBlockState();
         } else if (var4 == Class_0694.SOLID) {
            var3 = Blocks.BEDROCK.defaultBlockState();
         }

         Vec3 var5 = var1.method_543();
         Vec3 var6 = var1.method_544();
         VoxelShape var7 = var1.method_2(var3, mc.level, var2);
         BlockHitResult var8 = mc.level.clipWithInteractionOverride(var5, var6, var2, var7, var3);
         VoxelShape var9 = Shapes.empty();
         BlockHitResult var10 = var9.clip(var5, var6, var2);
         double var11 = var8 == null ? 1.7976931348623157e+308 : var1.method_543().distanceToSqr(var8.getLocation());
         double var13 = var10 == null ? 1.7976931348623157e+308 : var1.method_544().distanceToSqr(var10.getLocation());
         return var11 <= var13 ? var8 : var10;
      }, var0x -> {
         Vec3 var1 = var0x.method_543().subtract(var0x.method_544());
         return BlockHitResult.miss(var0x.method_543(), Direction.getNearest((int)Math.floor(var1.x), (int)Math.floor(var1.y), (int)Math.floor(var1.z), Direction.DOWN), BlockPos.containing(var0x.method_544()));
      });
   }

   public static HitResult method_2(Vec3 var0, float var1, float var2, float var3) {
      float var4 = var2 * Class_0245.field_690;
      float var5 = -var1 * Class_0245.field_690;
      float var6 = Mth.cos(var5);
      float var7 = Mth.sin(var5);
      float var8 = Mth.cos(var4);
      float var9 = Mth.sin(var4);
      Vec3 var10 = new Vec3((double)(var7 * var8), (double)(-var9), (double)(var6 * var8));
      Vec3 var11 = var0.add(var10.x * (double)var3, var10.y * (double)var3, var10.z * (double)var3);
      return mc.level.clip(new ClipContext(var0, var11, Block.OUTLINE, Fluid.NONE, mc.player));
   }

   public static int method_886() {
      BlockHitResult var0 = mc.level
         .clip(
            new ClipContext(
               mc.player.position(),
               new Vec3(mc.player.getX(), (double)mc.level.getMinY(), mc.player.getZ()),
               Block.COLLIDER,
               Fluid.NONE,
               mc.player
            )
         );
      return (int)Math.round(mc.player.getY() - var0.getLocation().y);
   }

   public static int method_887() {
      int var0 = (int)Math.round(mc.player.getY()) + 1;
      BlockHitResult var1 = mc.level
         .clip(
            new ClipContext(
               mc.player.position(),
               new Vec3(mc.player.getX(), (double)mc.level.getMaxY(), mc.player.getZ()),
               Block.COLLIDER,
               Fluid.NONE,
               mc.player
            )
         );
      return var1.getBlockPos().getY() - var0 - 1;
   }

   public static boolean getId(Entity var0) {
      AABB var1 = var0.getBoundingBox();

      for (float var2 = 0.0F; var2 <= 1F; var2 += 0.5F) {
         List var3 = BoxUtil.method_2(var1, var1.minY + (var1.maxY - var1.minY) * (double)var2);

         for (int var4 = 0; var4 < var3.size() - 1; var4++) {
            if (method_9(mc.player.getEyePosition(), (Vec3)var3.get(var4))) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean method_9(Vec3 var0, Vec3 var1) {
      return mc.level.clip(new ClipContext(var0, var1, Block.COLLIDER, Fluid.NONE, mc.player)).getType() == Type.MISS;
   }
}
