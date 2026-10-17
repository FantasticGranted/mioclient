package me.mioclient.record;

import me.mioclient.api.MinecraftAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

/** A block-face placement target: the facing direction, the hit vector, and the neighbor's collision shape. */
public final class FacePlacement implements MinecraftAccess {
   public final Direction direction;
   public final Vec3 hitVec;
   public final VoxelShape collisionShape;

   public FacePlacement(Direction var1, Vec3 var2, VoxelShape var3) {
      super();
      this.direction = var1;
      this.hitVec = var2;
      this.collisionShape = var3;
   }

   public static FacePlacement method_2(BlockFace var0, boolean var1) {
      Direction var2 = var0.getDirection();
      Vec3 var3 = var1 ? method_492(var0.getPos().relative(var2)) : var0.getPos().relative(var2).getCenter();
      VoxelShape var4 = mc.level.getBlockState(var0.getPos().relative(var2)).getCollisionShape(mc.level, BlockPos.ZERO);
      return new FacePlacement(var2, var3, var4);
   }

   public static Vec3 method_492(BlockPos var0) {
      return new Vec3(
         Mth.clamp(mc.player.getX(), (double)var0.getX(), (double)(var0.getX() + 1)),
         (double)var0.getY(),
         Mth.clamp(mc.player.getZ(), (double)var0.getZ(), (double)(var0.getZ() + 1))
      );
   }

   public Direction getDirection() {
      return this.direction;
   }

   public Vec3 method_564() {
      return this.hitVec;
   }

   public VoxelShape getShape() {
      return this.collisionShape;
   }
}
