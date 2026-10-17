package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.LongJumpModule;
import me.mioclient.module.movement.SpeedModule;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public final class Class_1376 extends SpeedMode {
   public static LongJumpModule field_1017 = Hub.field_2595.method_78(LongJumpModule.class);

   public Class_1376(SpeedModule var1) {
      super(var1);
   }

   @Override
   public void method_9(MoveEvent var1) {
      if (!this.method_404()) {
         if (this.field_2537.useTimer.getValue() && Hub.field_2602.method_984().method_9(250L)) {
            Hub.field_2596.method_2(this.field_2537, 1.0888F);
         } else {
            Hub.field_2596.method_38(this.field_2537);
         }

         if (!MovementUtil.method_363()) {
            var1.method_7(0.0, 0.0);
         } else {
            double var2 = MovementUtil.method_78(false);
            if (Hub.field_2602.method_984().method_9(500L)) {
               var2 = Math.max(var2, Math.hypot(var1.method_380(), var1.method_396()));
            }

            MovementUtil.method_2(var1, var2);
         }
      }
   }

   @Override
   public void method_738() {
      if (!this.method_404() && MovementUtil.method_363() && mc.player.verticalCollisionBelow) {
         double[] var1 = MovementUtil.method_2(mc.player.getYRot(), mc.player.input, 1.0);
         float var2 = (float)(Math.toDegrees(Math.atan2(var1[1], var1[0])) - (double)Class_0245.field_685);
         Vec3 var3 = mc.player.getDeltaMovement();
         float var4 = (float)Math.toRadians((double)var2);
         float var5 = 0.2F;
         mc.player.setDeltaMovement(var3.x, 0.4 + MovementUtil.method_495(), var3.z);
         mc.player.addDeltaMovement(new Vec3((double)(-Mth.sin(var4) * var5), 0.0, (double)(Mth.cos(var4) * var5)));
         mc.player.setSprinting(true);
      }
   }

   public boolean method_404() {
      return this.field_2537.method_526()
         || this.field_2537.method_525()
         || PlayerUtil.method_427()
         || mc.player.isFallFlying()
         || mc.player.isSpectator()
         || field_1017.isToggled();
   }
}
