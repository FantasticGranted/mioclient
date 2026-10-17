package me.mioclient.internal;

import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.combat.PusherModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class Class_0819 implements MinecraftAccess {
   public static final List<Block> field_2593 = List.of(Blocks.OBSIDIAN, Blocks.CRYING_OBSIDIAN, Blocks.REINFORCED_DEEPSLATE, Blocks.RESPAWN_ANCHOR);
   public final PusherModule field_2594;

   public Class_0819(PusherModule var1) {
      super();
      this.field_2594 = var1;
   }

   public Player method_691() {
      return mc.level
         .players()
         .stream()
         .filter(
            var1 -> var1.isAlive()
                  && var1 != mc.player
                  && EntityUtil.method_2(var1)
                  && mc.player.distanceTo(var1) <= this.field_2594.method_884()
                  && !Hub.field_2603.getId(var1)
         )
         .filter(var1 -> !this.field_2594.ignoreNaked.getValue() || PlayerUtil.method_29((LivingEntity)var1))
         .filter(this::method_52)
         .min(Comparator.comparing(var0 -> Mth.degreesDifferenceAbs(mc.player.getYRot(), RotationManager.method_14(var0)[0])))
         .orElse(null);
   }

   public boolean method_52(Player var1) {
      BlockPos var2 = var1.blockPosition();
      BlockState var3 = mc.level.getBlockState(var2.above());
      if (field_2593.contains(var3.getBlock())
         || var3.getDestroySpeed(mc.level, var2.above()) == -1F
         || Hub.field_2622.method_105(var2.above(2))) {
         return false;
      } else if (this.field_2594.onlyHole.getValue() && !Hub.field_2605.method_221(var2)) {
         return false;
      } else if (this.field_2594.onlySafe.getValue()
         && !Hub.field_2605.method_221(var2)
         && mc.level.getBlockState(var1.blockPosition()).getBlock().getExplosionResistance() < 6e+02F) {
         return false;
      } else {
         List var4 = PlayerUtil.method_5((LivingEntity)var1);
         return var4.size() <= 6;
      }
   }
}
