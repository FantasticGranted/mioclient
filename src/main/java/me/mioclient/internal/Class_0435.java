package me.mioclient.internal;

import me.mioclient.module.combat.CombatmineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public final class Class_0435 extends CombatMineMode {
   public Class_0435(CombatmineModule var1) {
      super(var1);
   }

   @Override
   public void method_2(BlockSelection var1) {
      Player var2 = this.field_2705.field_3711.method_691();
      if (var2 != null) {
         long var3 = BlockPos.betweenClosedStream(this.field_2705.field_3711.method_374(var2)).count();
         if (var3 == 1L) {
            BlockPos var5 = PlayerUtil.method_9(var2);
            if (!BlockUtil.method_14(var5)) {
               if (!BlockUtil.method_3(var5) && BlockUtil.method_3(var5.below())) {
                  var1.method_2(400, var2x -> {
                     if (this.field_2705.method_1047()) {
                        var2x.method_465(var5.below());
                     } else {
                        var2x.method_425(var5.below());
                     }
                  });
               }
            }
         }
      }
   }

   @Override
   public boolean method_206() {
      return this.field_2705.fish.getValue();
   }
}
