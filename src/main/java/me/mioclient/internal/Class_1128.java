package me.mioclient.internal;

import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import me.mioclient.Hub;
import me.mioclient.enum_.CombatmineMode;
import me.mioclient.module.abstract_.AbstractModule_8;
import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public final class Class_1128 extends CombatMineMode {
   public static AbstractModule_8 field_3483 = Hub.field_2595.method_78(AbstractModule_8.class);

   public Class_1128(CombatmineModule var1) {
      super(var1);
   }

   @Override
   public void method_2(BlockSelection var1) {
      Player var2 = this.field_2705.field_3711.method_691();
      if (var2 != null && !var2.isVisuallyCrawling() && !this.method_263(var2)) {
         AABB var3 = var2.getBoundingBox();
         boolean var4 = this.field_2705.method_1047();
         boolean var5 = BlockPos.betweenClosedStream(var3.setMaxY(var3.minY)).anyMatch(var0 -> InteractionUtil.getId(var0) == Blocks.BEDROCK);
         if (BlockPos.betweenClosedStream(
               var3.contract(
                  0.05, 0.05, 0.05
               )
            )
            .filter(
               var1x -> InteractionUtil.getId(var1x).getExplosionResistance() >= 6e+02F
                     && !this.method_513()
                     && InteractionUtil.getId(var1x) != Blocks.AIR
            )
            .toList()
            .isEmpty()) {
            AtomicReference var6 = new AtomicReference<>(
               BlockPos.betweenClosedStream(var3.setMaxY(var3.maxY + 0.5))
                  .<BlockPos>map(BlockPos::immutable)
                  .filter(this::method_123)
                  .max(Comparator.comparing(var1x -> this.method_494(var1x)))
                  .orElse(null)
            );
            AtomicReference var7 = new AtomicReference<>(
               PlayerUtil.method_5((LivingEntity)var2)
                  .stream()
                  .map(BlockPos::above)
                  .<BlockPos>map(BlockPos::immutable)
                  .filter(this::method_123)
                  .max(Comparator.comparing(var1x -> this.method_494(var1x)))
                  .orElse(null)
            );
            if (var6.get() == null && var7.get() != null && !this.method_513()) {
               var6.set((BlockPos)var7.get());
               var7.set(null);
            }

            if (var6.get() != null) {
               int var8 = 599;
               if (this.method_513()) {
                  if (var5) {
                     var8 = var4 ? 900 : 1000;
                  } else {
                     var8 = var4 ? 499 : 599;
                  }

                  var1.method_2(var8, var3x -> {
                     if (!var4) {
                        var3x.method_425((BlockPos)var6.get());
                     } else {
                        if (var7.get() != null) {
                           var3x.method_465((BlockPos)var6.get());
                           var3x.method_425((BlockPos)var7.get());
                        } else {
                           var3x.method_425((BlockPos)var6.get());
                        }
                     }
                  });
               } else {
                  boolean var9 = this.method_494((BlockPos)var6.get());
                  if (var9) {
                     var8 = 650;
                  }

                  if (this.field_2705.trapOverride.getValue() && field_3483.isToggled()) {
                     var8 = 650;
                  }

                  var1.method_2(var8, var1x -> var1x.method_425((BlockPos)var6.get()));
               }
            }
         }
      }
   }

   @Override
   public boolean method_206() {
      return this.field_2705.head.getValue();
   }

   public boolean method_123(BlockPos var1) {
      boolean var2 = InteractionUtil.getId(var1) == Blocks.OBSIDIAN || this.method_513();
      boolean var3 = mc.level.isEmptyBlock(var1.above()) || this.method_513();
      return (BlockUtil.method_3(var1) && var2 || this.method_494(var1)) && var3;
   }

   public boolean method_513() {
      return this.field_2705.logic.getValue() == CombatmineMode.GRIMV3;
   }
}
