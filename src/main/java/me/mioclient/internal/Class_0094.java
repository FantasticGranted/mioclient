package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.enum_.TakeoffMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.movement.ElytraFlyModule;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.util.Mth;

public class Class_0094 extends ElytraFlyBehavior {
   public long field_303;
   public boolean field_304;
   public boolean field_305;
   public boolean field_306;

   public Class_0094(ElytraFlyModule var1) {
      super(var1);
   }

   @Override
   public void onEnable() {
      if (mc.player.isFallFlying() && !mc.player.onGround()) {
         this.field_304 = true;
         mc.player.stopFallFlying();
      }

      this.field_305 = false;
      this.field_306 = false;
      this.field_303 = System.currentTimeMillis();
   }

   @Override
   public void method_2(PlayerTickEvent var1) {
      if (mc.player.onGround()) {
         this.field_304 = false;
      } else if (mc.player.isFallFlying()) {
         this.field_304 = true;
      }

      boolean var2 = mc.player.input.keyPresses.jump();
      if (this.field_2275.takeoff.getValue() == TakeoffMode.STRICT) {
         var2 = !mc.player.onGround() && mc.player.getDeltaMovement().y() < 0.1;
      } else {
         Hub.field_2596.method_38(this.field_2275);
      }

      if (var2 || this.field_305) {
         this.field_2275.method_1099();
         this.field_305 = !mc.player.isFallFlying();
      }
   }

   @Override
   public void method_2(MoveEvent var1) {
      if (!mc.player.isFallFlying() && this.field_304 && !mc.player.horizontalCollision && mc.player.getDeltaMovement().y() < 0.0) {
         this.field_305 = true;
      }

      if (mc.player.isFallFlying()) {
         if (!MovementUtil.method_363()) {
            this.field_303 = System.currentTimeMillis();
         }

         float var2 = this.field_2275.speed3.getValue();
         float var3 = this.field_2275.start.getValue();
         float var4 = Mth.clamp(
            (float)(System.currentTimeMillis() - this.field_303) / 5e+02F, 0.0F, 1F
         );
         float var5 = Mth.lerp(var4, var3, var2);
         double[] var6 = MovementUtil.method_2(var1, (double)var5);
         mc.player.setDeltaMovement(var6[0], 0.0, var6[1]);
         var1.setY(0.0);
         mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, 0.0));
      }
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket && mc.player.isFallFlying() && !mc.player.onGround()) {
         this.field_303 = System.currentTimeMillis();
         mc.player.stopFallFlying();
      }
   }

   @Override
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket var2
         && mc.player.isFallFlying()
         && !mc.player.onGround()
         && mc.player.getDeltaMovement().y() < 0.1
         && MovementUtil.method_363()) {
         ((DuckPlayerMoveC2SPacket)(Object)var2).setPitch(-5F);
      }
   }

   @Override
   public void method_9(Event_19 var1) {
      if (var1.method_213() == PreType.PRE) {
         boolean var2 = false;
         if (this.field_2275.method_558() && !var1.method_707()) {
            if (!mc.player.isFallFlying()) {
               var2 = true;
               this.field_306 = false;
            } else if (!this.field_306) {
               if (!MovementUtil.method_363()) {
                  var2 = true;
               } else {
                  this.field_306 = true;
               }
            }
         }

         if (var2) {
            var1.setYaw(var1.method_500() + (float)(mc.player.tickCount % 2 == 0 ? -2 : 2));
            var1.setPitch(var1.method_501() + (float)(mc.player.tickCount % 2 == 0 ? -2 : 2));
         }
      }
   }

   @Override
   public void method_2(PostTickEvent var1) {
   }

   @Override
   public void method_9(TravelEvent var1) {
   }
}
