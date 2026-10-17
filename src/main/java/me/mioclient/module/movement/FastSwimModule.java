package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.player.SpeedMineModule;
import me.mioclient.setting.Setting;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import me.mioclient.nick.Settings;

public class FastSwimModule extends Module {
   public static SpeedMineModule speedmine = Hub.field_2595.method_78(SpeedMineModule.class);
   public Setting<Boolean> water;
   public Setting<Float> speed;
   public Setting<Boolean> lava;
   public Setting<Float> speed2;
   public Setting<Boolean> vertical;
   public Setting<Float> vSpeed;
   public Setting<Float> glide;
   public Setting<Boolean> accelerate;
   public Setting<Float> accelMin;
   public Setting<Float> accelTime;
   public long field_303;
   public boolean field_4445;

   public FastSwimModule() {
      super("FastSwim", "Makes you move faster while in liquids.", Category.MOVEMENT);
      Settings.initialize(this);
      this.speed.method_31("WaterSpeed");
      this.speed2.method_31("LavaSpeed");
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if ((mc.player.isInLava() && this.lava.getValue() || PlayerUtil.method_7(mc.player) && this.water.getValue())
         && !mc.player.isFallFlying()
         && mc.player.onGround()) {
         mc.player.avatarState().walkDist = 0.0F;
      }
   }

   @Subscribe
   public void method_9(MoveEvent var1) {
      boolean var2 = speedmine.isToggled()
         && (double)speedmine.method_1119() + speedmine.method_1114()
            >= (double)speedmine.damage.getValue().floatValue() - 0.1
         && speedmine.method_1118() != null
         && BlockUtil.method_3(speedmine.method_1118());
      if ((mc.player.isInLava() && this.lava.getValue() || PlayerUtil.method_7(mc.player) && this.water.getValue())
         && !mc.player.isFallFlying()
         && Hub.field_2602.method_984().method_9(500L)
         && !var2) {
         if (!MovementUtil.method_363()) {
            this.field_303 = System.currentTimeMillis();
         }

         float var3 = this.vertical.getValue() ? 0.12F * this.vSpeed.getValue() : 0.0F;
         float var4 = 0.12F * this.accelMin.getValue();
         double var5 = (double)(0.12F * this.speed2.getValue());
         boolean var7 = this.water.getValue() && PlayerUtil.method_7(mc.player);
         if (var7) {
            var5 = (double)(0.12F * this.speed.getValue());
         }

         if (this.accelerate.getValue() && (double)var4 < var5) {
            float var8 = Mth.clamp(
               (float)(System.currentTimeMillis() - this.field_303) / (this.accelTime.getValue() * 1e+03F),
               0.0F,
               1F
            );
            double var9 = (double)var4 + (var5 - (double)var4) * (double)var8;
            var5 = Math.min(var9, var5);
         }

         if (mc.player.input.keyPresses.jump() && !this.field_4445) {
            var1.setY((double)var3);
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, (double)var3));
         } else if (mc.player.input.keyPresses.shift()) {
            var1.setY((double)(-var3));
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, (double)(-var3)));
            var5 /= 2.0;
         } else {
            var1.setY(0.0);
            mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, 0.0));
            if (!mc.player.verticalCollision && this.glide.getValue() != 0.0F) {
               mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0.0, (double)(-this.glide.getValue()), 0.0));
               var1.setY(mc.player.getDeltaMovement().y);
            }
         }

         double[] var11 = MovementUtil.method_2(var1, var5);
         mc.player.setDeltaMovement(var11[0], mc.player.getDeltaMovement().y, var11[1]);
      } else {
         this.field_303 = System.currentTimeMillis();
      }
   }
}
