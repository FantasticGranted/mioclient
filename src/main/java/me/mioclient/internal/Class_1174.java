package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;

public final class Class_1174 extends CombatMineMode {
   public Class_1174(CombatmineModule var1) {
      super(var1);
   }

   @Override
   public void method_2(BlockSelection var1) {
      BlockPos var2 = null;

      for (BlockEntity var4 : Hub.field_2622.method_347()) {
         if (!(mc.player.getEyePosition().distanceTo(var4.getBlockPos().getCenter()) > 2.2)
            && var4 instanceof EnderChestBlockEntity
            && this.method_493(var4.getBlockPos())
            && (var2 == null || this.method_494(var4.getBlockPos()))) {
            var2 = var4.getBlockPos();
         }
      }

      BlockPos var5 = var2;
      if (var5 != null) {
         var1.method_2(5, var1x -> var1x.method_425(var5));
      }
   }

   @Override
   public boolean method_206() {
      return this.field_2705.enderChests.getValue();
   }
}
