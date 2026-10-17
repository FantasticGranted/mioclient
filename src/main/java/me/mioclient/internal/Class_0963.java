package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.movement.SpeedModule;
import net.minecraft.client.player.LocalPlayer;

public final class Class_0963 extends SpeedMode {
   public Class_0963(SpeedModule var1) {
      super(var1);
      this.field_1571 = 2;
   }

   @Override
   public void method_9(MoveEvent var1) {
      if (this.method_670()) {
         this.field_2537.reset();
         MovementUtil.method_2(var1, MovementUtil.method_78(true));
      } else {
         if (this.field_1571 == 4) {
            this.field_2537.reset();
         }

         if (mc.player.onGround() || this.field_1571 == 3) {
            if (!mc.player.horizontalCollision && mc.player.zza != 0.0F || mc.player.xxa != 0.0F) {
               if (this.field_1571 == 2) {
                  this.field_1570 = this.field_1570 * 2.149;
                  this.field_1571 = 3;
               } else if (this.field_1571 == 3) {
                  this.field_1571 = 2;
                  this.field_1570 = this.field_1419 - 0.66 * (this.field_1419 - MovementUtil.method_78(true));
               }
            }

            this.field_1570 = Math.min(this.field_1570, (double)this.field_2537.speed.getValue().floatValue());
            this.field_1570 = Math.max(this.field_1570, MovementUtil.method_78(true));
            double[] var2 = MovementUtil.method_2(mc.player.getViewYRot(RenderUtil.method_776()), mc.player.input, this.field_1570);
            if (!mc.level.noCollision(mc.player.getBoundingBox().expandTowards(var2[0], 0.0, var2[1]))) {
               this.field_2537.reset();
               this.field_1570 = MovementUtil.method_78(true);
            }

            MovementUtil.method_2(var1, this.field_1570);
         }
      }
   }

   @Override
   public void method_2(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (!this.method_670()) {
            super.method_2(var1);
            if (this.field_1571 == 3) {
               var1.setY(
                  var1.method_395()
                     + (this.method_739() ? 0.2 : 0.4)
                     + MovementUtil.method_495()
               );
            }
         }
      }
   }

   public boolean method_670() {
      LocalPlayer var1 = mc.player;
      double var2 = var1.getX() - var1.xo;
      double var4 = var1.getY() - var1.yo;
      double var6 = var1.getZ() - var1.zo;
      if (!mc.level.noCollision(mc.player.getBoundingBox().move(var2, var4, var6))) {
         return true;
      } else {
         return mc.level.noCollision(mc.player.getBoundingBox().expandTowards(0.0, -1.0, 0.0))
            ? true
            : mc.player.horizontalCollision
               || mc.player.getY() > Math.floor(mc.player.getY())
               || !this.method_864()
               || Hub.field_2612.method_2(mc.player, 3).minY < mc.player.getY();
      }
   }

   public boolean method_864() {
      return Hub.field_2602.method_984().method_9(500L);
   }
}
