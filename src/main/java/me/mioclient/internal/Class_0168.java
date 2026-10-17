package me.mioclient.internal;

import java.util.Comparator;
import java.util.List;
import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public final class Class_0168 extends CombatMineMode {
   public Class_0168(CombatmineModule var1) {
      super(var1);
   }

   @Override
   public void method_2(BlockSelection var1) {
      boolean var2 = this.field_2705.method_1047();
      Player var3 = this.field_2705.field_3711.method_691();
      if (var3 != null) {
         List var4 = BlockPos.betweenClosedStream(var3.getBoundingBox().inflate(-BoxUtil.field_2280, 0.0, -BoxUtil.field_2280).setMaxY(var3.getY()))
            .<BlockPos>map(BlockPos::immutable)
            .filter(var1x -> this.method_493(var1x))
            .filter(var0 -> {
               Block var1x = mc.level.getBlockState(var0).getBlock();
               return var1x.getExplosionResistance() >= 6e+02F && var1x != Blocks.COBWEB && var1x != Blocks.ANVIL;
            })
            .sorted(
               Comparator.comparing(
                  var2x -> this.method_494(var2x) ? -1.0 : var3.distanceToSqr(var2x.getCenter())
               )
            )
            .toList();
         if (!var4.isEmpty()) {
            short var5 = 700;
            if (var2 && var4.size() == 1) {
               var5 = 500;
            }

            var1.method_2(var5, var2x -> {
               if (!var2) {
                  var2x.method_425((BlockPos)var4.getFirst());
               } else {
                  if (var4.size() == 1) {
                     var2x.method_465((BlockPos)var4.getFirst());
                  } else {
                     var2x.method_425((BlockPos)var4.getFirst());
                     var2x.method_465((BlockPos)var4.get(1));
                  }
               }
            });
         }
      }
   }

   @Override
   public boolean method_206() {
      return this.field_2705.field_3699.getValue();
   }
}
