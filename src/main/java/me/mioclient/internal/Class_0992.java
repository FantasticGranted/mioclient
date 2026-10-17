package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.LongJumpModule;
import me.mioclient.module.movement.SpeedModule;

public class Class_0992 extends SpeedMode {
   public static LongJumpModule field_1017 = Hub.field_2595.method_78(LongJumpModule.class);

   public Class_0992(SpeedModule var1) {
      super(var1);
      this.field_1571 = 4;
   }

   @Override
   public void method_9(MoveEvent var1) {
      if (!this.field_2537.method_526()
         && !this.field_2537.method_525()
         && !PlayerUtil.method_427()
         && !mc.player.isFallFlying()
         && !mc.player.isSpectator()
         && !field_1017.isToggled()) {
         double var6 = var1.method_395();
         if (this.field_2537.useTimer.getValue() && Hub.field_2602.method_984().method_9(250L)) {
            Hub.field_2596.method_2(this.field_2537, 1.0888F);
         } else if (this.field_2537.useTimer.getValue()) {
            Hub.field_2596.method_38(this.field_2537);
         }

         float var8 = !this.field_2537.field_2198.method_9(1000L) && this.field_2537.boost.getValue()
            ? 1.2F
            : 1F;
         if (this.field_1571 == 1 && MovementUtil.method_363()) {
            this.field_1570 = 1.35
                  * MovementUtil.method_2(
                     false, 0.2873 * (double)this.field_2537.speed.getValue().floatValue() * (double)var8
                  )
               - 0.01;
         } else if (this.field_1571 == 2 && MovementUtil.method_363() && mc.player.verticalCollisionBelow) {
            var6 = 0.42 + MovementUtil.method_495();
            this.field_1570 = this.field_1570 * 1.535;
         } else if (this.field_1571 == 3) {
            this.field_1570 = this.field_1419
               - 0.6600000262260437
                  * (
                     this.field_1419
                        - MovementUtil.method_2(
                           true, 0.2873 * (double)this.field_2537.speed.getValue().floatValue() * (double)var8
                        )
                  );
         } else {
            if ((this.method_739() || mc.player.verticalCollision) && this.field_1571 > 0) {
               this.field_1571 = MovementUtil.method_363() ? 1 : 0;
            }

            this.field_1570 = this.field_1419 - this.field_1419 / 159.0;
         }

         this.field_1570 = Math.max(
            this.field_1570,
            MovementUtil.method_2(
               false, 0.2873 * (double)this.field_2537.speed.getValue().floatValue() * (double)var8
            )
         );
         double var2;
         double var4;
         if (!MovementUtil.method_363()) {
            var2 = 0.0;
            var4 = 0.0;
         } else {
            double[] var9 = MovementUtil.method_2(mc.player.getViewYRot(RenderUtil.method_776()), mc.player.input, this.field_1570);
            var2 = var9[0];
            var4 = var9[1];
         }

         VectorUtil.method_2(var1.method_1066(), var2, var6, var4);
         if (MovementUtil.method_363()) {
            this.field_1571++;
         }
      }
   }
}
