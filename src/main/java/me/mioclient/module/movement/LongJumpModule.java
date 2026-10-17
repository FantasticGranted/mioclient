package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.VectorUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class LongJumpModule extends Module {
   public Setting<Float> jumpSpeed;
   public Setting<Boolean> inLiquid;
   public Setting<Boolean> useTimer;
   public Setting<Boolean> autoDisable;
   public double field_1570;
   public double field_1419;
   public int field_1571;
   public boolean field_1572;

   public LongJumpModule() {
      super("LongJump", "Makes you jump fast 'n far.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_1571 = 4;
   }

   @Subscribe
   public void method_9(MoveEvent var1) {
      if (!this.method_526() && !this.method_525() && !mc.player.isFallFlying() && !mc.player.isSpectator()) {
         double var6 = var1.method_395();
         if (this.useTimer.getValue() && Hub.field_2602.method_984().method_9(250L)) {
            Hub.field_2596.method_2(this, 1.0888F);
         } else if (this.useTimer.getValue()) {
            Hub.field_2596.method_38(this);
         }

         if (!Hub.field_2602.method_984().method_9(250L) && this.autoDisable.getValue()) {
            this.method_68();
         }

         double var8 = (double)(2F * this.jumpSpeed.getValue());
         if (this.field_1571 == 1 && MovementUtil.method_363()) {
            this.field_1570 = 1.35 * MovementUtil.method_2(false, 0.2873 * var8)
               - 0.01;
         } else if (this.field_1571 == 2 && MovementUtil.method_363()) {
            var6 = 0.42 + MovementUtil.method_495();
            this.field_1570 = this.field_1570
               * (this.field_1572 ? 1.6835 : 1.395);
         } else if (this.field_1571 == 3) {
            this.field_1570 = this.field_1419
               - 0.6600000262260437
                  * (this.field_1419 - MovementUtil.method_2(true, 0.2873 * var8));
            this.field_1572 = !this.field_1572;
         } else {
            if ((
                  mc.player.verticalCollision
                     || !mc.level.noCollision(mc.player.getBoundingBox().move(0.0, mc.player.getDeltaMovement().y, 0.0))
               )
               && this.field_1571 > 0) {
               this.field_1571 = MovementUtil.method_363() ? 1 : 0;
            }

            this.field_1570 = this.field_1419 - this.field_1419 / 159.0;
         }

         this.field_1570 = Math.max(this.field_1570, MovementUtil.method_2(false, 0.2873 * var8));
         double var2;
         double var4;
         if (!MovementUtil.method_363()) {
            var2 = 0.0;
            var4 = 0.0;
         } else {
            double[] var10 = MovementUtil.method_2(mc.player.getViewYRot(RenderUtil.method_776()), mc.player.input, this.field_1570);
            var2 = var10[0];
            var4 = var10[1];
         }

         VectorUtil.method_2(var1.method_1066(), var2, var6, var4);
         if (MovementUtil.method_363()) {
            this.field_1571++;
         }
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_2(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         if (!MovementUtil.method_363()) {
            this.field_1571 = 4;
         }

         double var2 = mc.player.getX() - mc.player.xo;
         double var4 = mc.player.getZ() - mc.player.zo;
         this.field_1419 = Math.sqrt(var2 * var2 + var4 * var4);
      }
   }

   public boolean method_525() {
      return PlayerUtil.method_29(mc.player) && !this.inLiquid.getValue();
   }

   public boolean method_526() {
      AABB var1 = mc.player.getBoundingBox();
      AABB var2 = new AABB(
            (double)mc.player.blockPosition().getX(),
            var1.minY,
            (double)mc.player.blockPosition().getZ(),
            (double)mc.player.blockPosition().getX() + 1.0,
            var1.maxY,
            (double)mc.player.blockPosition().getZ() + 1.0
         )
         .deflate(1e-07);
      return mc.level.collidesWithSuffocatingBlock(mc.player, var2);
   }
}
