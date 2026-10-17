package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.module.abstract_.AbstractModule_42;
import me.mioclient.record.SafetyBox;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;

public class Class_0700 extends Class_0351 {
   public Class_0700(AbstractModule_42 var1) {
      super(var1);
   }

   @Override
   public boolean method_2(SafetyBox var1) {
      for (AbstractClientPlayer var3 : mc.level.players()) {
         if (!this.field_1150.ignoreNakeds.getValue() || PlayerUtil.method_29(var3)) {
            AABB var4 = this.field_1150.extrapolation.getValue()
               ? Hub.field_2612.method_2(var3, BoxUtil.method_2(var3, this.field_1150.ticks))
               : BoxUtil.method_4(var3);
            boolean var5 = !MovementUtil.method_9(var3) || MovementUtil.method_2(var3, var1.getPos().getCenter());
            if (!(VectorUtil.method_5(var1.getPos().getCenter(), BoxUtil.method_2(var4)) > this.field_1150.smartRange.getValue())
               && !(var3.getY() <= (double)var1.getPos().getY())
               && !(var4.minY - (double)var1.getPos().getY() > 4.0)
               && !var3.isDeadOrDying()
               && mc.player != var3
               && !Hub.field_2603.method_1009(var3.getGameProfile().name())
               && !Hub.field_2605.method_221(BlockPos.containing(var3.position()))
               && var5
               && (
                  !this.field_1150.selfSafety.getValue()
                     || Hub.field_2605.method_221(BlockPos.containing(mc.player.position()))
                     || !(mc.player.position().distanceTo(var1.getPos().getCenter()) < 2.0)
               )) {
               return super.method_2(var1);
            }
         }
      }

      return false;
   }
}
