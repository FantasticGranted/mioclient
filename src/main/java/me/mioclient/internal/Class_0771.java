package me.mioclient.internal;

import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.CombatmineMode;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.combat.CombatmineModule;
import me.mioclient.module.player.SpeedMineModule;
import me.mioclient.record.BlockPosPair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public final class Class_0771 extends CombatMineMode {
   public static AutoCrystalModule autoCrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   public static SpeedMineModule speedmine = Hub.field_2595.method_78(SpeedMineModule.class);
   public static final List<Vec3i> field_2427 = List.of(new Vec3i(1, 0, 0), new Vec3i(0, 0, 1), new Vec3i(0, 1, 0), new Vec3i(0, 0, -1), new Vec3i(-1, 0, 0));

   public Class_0771(CombatmineModule var1) {
      super(var1);
   }

   @Override
   public void method_2(BlockSelection var1) {
      Player var2 = this.field_2705.field_3711.method_691();
      boolean var3 = this.field_2705.fish.getValue() && var1.method_52() == 400 && var1.method_897() != null;
      if (var2 != null && !this.method_263(var2) && (!this.method_425(var2) || var3)) {
         BlockPos var4 = BlockPos.containing(var2.position());
         List var5 = PlayerUtil.method_5((LivingEntity)var2);
         Block var6 = mc.level.getBlockState(var4).getBlock();
         boolean var7 = var6.getExplosionResistance() >= 6e+02F && var6 != Blocks.AIR;
         if (!this.field_2705.field_3712.method_29(500L) || var4.equals(this.field_2705.field_3712.method_111())) {
            var7 = false;
         }

         BlockPosPair var8 = this.method_2(var5, var7, var3);
         if (var8.start != null) {
            var1.method_2(600, var1x -> {
               if (var1x.method_897() == null) {
                  var1x.method_465(var8.end);
               }

               var1x.method_425(var8.start);
            });
         }
      }
   }

   public BlockPosPair method_2(List<BlockPos> var1, boolean var2, boolean var3) {
      BlockPos var4 = null;
      BlockPos var5 = null;
      boolean var6 = false;
      var1.sort(Comparator.comparing(var1x -> {
         if (mc.player.getBoundingBox().intersects(new AABB(var1x))) {
            return -9999.0;
         } else if (PlayerUtil.method_5((LivingEntity)mc.player).contains(var1x)) {
            return -999.0;
         } else {
            boolean var2x = false;

            for (Vec3i var4x : field_2427) {
               if (var4x.getY() == 0) {
                  BlockPos var5x = var1x.offset(var4x);
                  if (this.method_15(var5x.below())) {
                     var2x = true;
                     break;
                  }
               }
            }

            double var6x = var1x.distToCenterSqr(mc.player.position());
            if (!var2x) {
               var6x += Math.pow((double)speedmine.range.method_101().floatValue(), 2.0);
            }

            return -var6x;
         }
      }));

      for (BlockPos var8 : var1) {
         if (var3) {
            var8 = var8.below();
         }

         boolean var9 = BlockUtil.method_14(var8) && var8.equals(speedmine.method_1120());
         boolean var10 = this.field_2705.logic.getValue() == CombatmineMode.GRIM || this.field_2705.logic.getValue() == CombatmineMode.GRIMV3;
         if (var2) {
            var9 = false;
         }

         if (speedmine.method_4(400L) && var10) {
            var9 = false;
         }

         if ((this.method_323(var8) || var3 || var10) && (this.method_493(var8) || var9)) {
            if (this.method_494(var8)) {
               var4 = var8;
               var6 = true;
            } else if (var6) {
               var5 = var8;
            } else {
               var5 = var4;
               var4 = var8;
            }
         }
      }

      return new BlockPosPair(var4, var5);
   }

   public boolean method_323(BlockPos var1) {
      for (Vec3i var3 : field_2427) {
         BlockPos var4 = var1.offset(var3);
         if (this.method_15(var4.below())) {
            return true;
         }
      }

      return false;
   }

   public boolean method_231(BlockPos var1) {
      if (this.field_2705.safe.getValue()) {
         for (BlockPos var3 : PlayerUtil.method_5((LivingEntity)mc.player)) {
            if (var3.equals(var1)) {
               return true;
            }
         }
      }

      List var4 = BlockPos.betweenClosedStream(mc.player.getBoundingBox().setMaxY(mc.player.getY()))
         .<BlockPos>map(BlockPos::immutable)
         .filter(var0 -> mc.level.getBlockState(var0).getBlock().getExplosionResistance() >= 6e+02F)
         .toList();
      return var4.size() == 1 && var4.contains(var1);
   }

   public boolean method_425(Player var1) {
      return BlockPos.betweenClosedStream(var1.getBoundingBox().setMaxY(var1.getY()))
         .<BlockPos>map(BlockPos::immutable)
         .allMatch(var0 -> mc.level.getBlockState(var0).is(Blocks.BEDROCK));
   }

   public boolean method_15(BlockPos var1) {
      return InteractionUtil.method_2(var1, autoCrystal.field_4058.getValue(), true, false, false, autoCrystal.lowerHitBox.getValue(), autoCrystal.hBFix.getValue());
   }

   @Override
   public boolean method_206() {
      return this.field_2705.feet.getValue();
   }

   @Override
   public boolean method_493(BlockPos var1) {
      if (this.method_231(var1)) {
         return false;
      } else {
         return BlockUtil.method_33(var1) == Blocks.COBWEB ? false : super.method_493(var1);
      }
   }
}
