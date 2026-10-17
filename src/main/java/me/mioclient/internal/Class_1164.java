package me.mioclient.internal;

import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public final class Class_1164 extends CombatMineMode {
   public Class_1164(CombatmineModule var1) {
      super(var1);
   }

   @Override
   public void method_2(BlockSelection var1) {
      BlockPos var2 = this.method_1028();
      if ((this.field_2705.field_3711.method_691() != null || var2 != null) && !mc.player.isFallFlying()) {
         AABB var3 = mc.player.getBoundingBox();
         if (!this.field_2705.onlyCrawl.getValue() || this.method_1029() || var2 != null) {
            int var4 = !this.method_1029() && var2 == null ? 100 : 1500;
            BlockPos var5 = this.method_38(var3);
            if (var5 != null) {
               var1.method_2(var4, var1x -> var1x.method_425(var5));
            }
         }
      }
   }

   public BlockPos method_38(AABB var1) {
      BlockPos var2 = BlockPos.containing(mc.player.position()).below();
      BlockPos var3 = this.method_1028();
      if (this.method_493(var2) && (this.method_494(var2) || this.method_1029()) && this.method_1030()) {
         return var2;
      } else {
         var2 = BlockPos.containing(mc.player.position().add(0.0, var1.getYsize() - BoxUtil.field_2280, 0.0)).above();
         if (var3 != null) {
            var2 = var3;
         }

         return this.method_493(var2) ? var2 : null;
      }
   }

   public BlockPos method_1028() {
      if (!this.field_2705.face.getValue()) {
         return null;
      } else {
         BlockPos var1 = BlockPos.containing(mc.player.getEyePosition());
         BlockState var2 = mc.level.getBlockState(var1);
         return !var2.isAir() && !(var2.getBlock().getExplosionResistance() < 6e+02F) && !this.method_1029() && this.method_493(var1)
            ? var1
            : null;
      }
   }

   public boolean method_1029() {
      return mc.player.getBoundingBox().getYsize() <= 1.0;
   }

   public boolean method_1030() {
      if (this.field_2705.downPriority.getValue()) {
         return true;
      } else if (mc.level.isEmptyBlock(PlayerUtil.method_425().below(2))) {
         return false;
      } else {
         for (BlockPos var2 : PlayerUtil.method_5((net.minecraft.world.entity.LivingEntity)mc.player)) {
            BlockState var3 = mc.level.getBlockState(var2.below());
            if (var3.isAir() || var3.getBlock().getExplosionResistance() < 6e+02F) {
               return false;
            }
         }

         return true;
      }
   }

   @Override
   public boolean method_206() {
      return this.field_2705.self.getValue();
   }
}
