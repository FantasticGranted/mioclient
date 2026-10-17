package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.ClientFunction;
import me.mioclient.module.abstract_.AbstractModule_13;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.phys.Vec3;

public class Class_0470 implements ClientFunction<LivingEntity, BlockPos> {
   public final AbstractModule_13 field_1490;
   public Vec3 field_1491;
   public float field_1492;
   public float field_1493;
   public float field_1494;

   public Class_0470(AbstractModule_13 var1) {
      super();
      this.field_1490 = var1;
   }

   public void method_142() {
      this.field_1491 = mc.player.getEyePosition();
      this.field_1492 = this.field_1490.range2.getValue();
      this.field_1493 = this.field_1490.minDamage.getValue();
      this.field_1494 = this.field_1490.maxSelfDamage.getValue();
   }

   @Override
   public BlockPos method_35(LivingEntity var1) {
      return this.method_31(var1);
   }

   public BlockPos method_31(LivingEntity var1) {
      this.method_142();
      float var2 = 0.0F;
      BlockPos var3 = null;

      for (BlockEntity var5 : Hub.field_2622.method_347()) {
         if (var5 instanceof BedBlockEntity) {
            BedPart var6 = (BedPart)var5.getBlockState().getValue(BedBlock.PART);
            if (var6 != BedPart.FOOT && !(var5.getBlockPos().getCenter().distanceTo(this.field_1491) > (double)this.field_1492)) {
               float var7 = ExplosionDamage.method_9(var5.getBlockPos().getCenter(), var1);
               float var8 = ExplosionDamage.method_9(var5.getBlockPos().getCenter(), mc.player);
               if ((!(var8 >= EntityUtil.method_76()) || !this.field_1490.antiSucide.getValue())
                  && !(var8 > this.field_1494)
                  && !(var7 < this.field_1493)
                  && (var3 == null || var7 > var2)) {
                  var3 = var5.getBlockPos();
                  var2 = var7;
               }
            }
         }
      }

      return var3;
   }
}
