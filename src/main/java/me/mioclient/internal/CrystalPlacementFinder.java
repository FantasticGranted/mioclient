package me.mioclient.internal;

import java.util.List;
import me.mioclient.api.ClientFunction;
import me.mioclient.enum_.Class_1172;
import me.mioclient.module.abstract_.AbstractModule_13;
import me.mioclient.record.CrystalPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

/**
 * Searches blocks around a target for the best end-crystal placement, scoring each
 * candidate by explosion damage vs self-damage and returning the optimal placement.
 */
public class CrystalPlacementFinder implements ClientFunction<LivingEntity, CrystalPlacement> {
   public static final MutableBlockPos posCursor = new MutableBlockPos();
   public final AbstractModule_13 module;
   public Direction placeDirection;
   public Vec3 hitVec;
   public float field_1492;

   public CrystalPlacementFinder(AbstractModule_13 var1) {
      super();
      this.module = var1;
   }

   @Override
   public CrystalPlacement method_35(LivingEntity var1) {
      return this.method_22(var1);
   }

   public CrystalPlacement method_22(LivingEntity var1) {
      this.field_1492 = this.module.range.getValue();
      Vec3 var2 = var1.position();
      float var3 = 2F;
      CrystalPlacement var4 = null;

      for (float var5 = -var3; var5 < var3; var5 += 1F) {
         for (float var6 = -var3; var6 < 3F; var6 += 1F) {
            for (float var7 = -var3; var7 < var3; var7 += 1F) {
               this.hitVec = null;
               posCursor.set(var2.x + (double)var5, var2.y + (double)var6, var2.z + (double)var7);
               CrystalPlacement var8 = this.method_2(var1, posCursor);
               if (var8 != null && (var4 == null || var8.getDamage() >= var4.getDamage())) {
                  var4 = var8;
               }
            }
         }
      }

      return var4;
   }

   public CrystalPlacement method_2(LivingEntity var1, BlockPos var2) {
      if (!mc.level.getBlockState(var2).canBeReplaced() && !this.module.method_2(var2, BedPart.HEAD)) {
         return null;
      } else {
         BlockPos var3 = null;
         Direction var4 = null;

         for (Direction var6 : Plane.HORIZONTAL) {
            BlockPos var7 = var2.relative(var6);
            if (InteractionUtil.method_9(var7, true, false) || this.module.method_2(var7, BedPart.FOOT)) {
               var4 = this.method_2(var6.getOpposite(), var7, this.module.strictDirection.getValue());
               if (var4 != null) {
                  this.placeDirection = var6.getOpposite();
                  var3 = var7;
                  break;
               }
            }
         }

         if (var3 == null) {
            return null;
         } else {
            float var8 = ExplosionDamage.method_9(var2.getCenter(), var1, this.module.method_16(var1));
            if (var8 < this.module.minDamage.getValue()) {
               return null;
            } else {
               float var9 = ExplosionDamage.method_9(var2.getCenter(), mc.player);
               if (var9 > this.module.maxSelfDamage.getValue()) {
                  return null;
               } else {
                  if (this.hitVec == null && !this.module.rotate.getValue()) {
                     this.hitVec = var3.getCenter().relative(var4, 0.5);
                  }

                  return new CrystalPlacement(var3, var2.immutable(), var8, var9, this.method_2(var4, var3));
               }
            }
         }
      }
   }

   public Direction method_2(Direction var1, BlockPos var2, boolean var3) {
      boolean var4 = this.module.airPlace.getValue();
      Direction[] var5 = Direction.values();
      int var6 = var5.length;
      int var7 = 0;

      Direction var8;
      while (true) {
         if (var7 >= var6) {
            return null;
         }

         var8 = var5[var7];
         BlockPos var9 = var2.relative(var8);
         if (var9.getY() < mc.level.getMaxY()
            && (BlockUtil.method_37(var9) || var4)
            && (!var3 || InteractionUtil.method_39(var9).contains(var8.getOpposite()))) {
            if (!this.module.rotate.getValue()) {
               break;
            }

            this.hitVec = this.method_2(var8, var1, var2);
            if (this.hitVec != null) {
               break;
            }
         }

         var7++;
      }

      return var8;
   }

   public Vec3 method_2(Direction var1, Direction var2, BlockPos var3) {
      for (Vec3 var5 : this.method_38(var3, var1)) {
         if (!(var5.distanceTo(mc.player.getEyePosition()) > (double)this.field_1492)) {
            float var6 = RotationManager.method_78(var5)[0];
            Direction var7 = Direction.fromYRot((double)var6);
            if (var2 == var7) {
               return var5;
            }
         }
      }

      return null;
   }

   public List<Vec3> method_38(BlockPos var1, Direction var2) {
      return var2 == null ? Class_1172.field_3634.method_38(var1) : Class_1172.field_3634.method_2(var1, var2);
   }

   public BlockHitResult method_2(Direction var1, BlockPos var2) {
      if ((!this.module.airPlace.getValue() || BlockUtil.method_37(var2.relative(var1)))
         && (!this.module.liquidPlace.getValue() || !BlockUtil.method_17(var2))) {
         return new BlockHitResult(this.hitVec, var1.getOpposite(), var2.relative(var1), false);
      } else {
         this.hitVec = this.method_2(null, this.placeDirection, var2);
         return new BlockHitResult(this.hitVec, var1, var2, false);
      }
   }
}
