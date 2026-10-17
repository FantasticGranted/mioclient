package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.enum_.ElytraVerticalMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.abstract_.AbstractModule_6;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.movement.FireworksModule;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class Class_0933 extends ElytraFlyBehavior {
   public static final AbstractModule_6 field_2917 = Hub.field_2595.method_78(AbstractModule_6.class);
   public static FireworksModule fireworks = Hub.field_2595.method_78(FireworksModule.class);
   public Vec3 field_2918 = Vec3.ZERO;
   public int field_31;
   public double field_1570;
   public long field_303;
   public long field_2919;

   public Class_0933(ElytraFlyModule var1) {
      super(var1);
   }

   @Override
   public void onEnable() {
      this.field_1570 = 0.0;
      this.field_2919 = this.field_303 = System.currentTimeMillis();
   }

   @Override
   public void method_2(PlayerTickEvent var1) {
      if (!mc.player.isFallFlying()) {
         this.field_2919 = System.currentTimeMillis();
      }

      if (!mc.player.input.keyPresses.jump() && !mc.player.input.keyPresses.shift()) {
         this.field_2919 = System.currentTimeMillis();
      }
   }

   @Override
   public void method_2(MoveEvent var1) {
      if (!this.method_557()) {
         if (!mc.player.isFallFlying() || this.field_2275.method_1182()) {
            this.field_303 = System.currentTimeMillis();
         } else if (this.method_847() && this.field_2275.method_558()) {
            if (!MovementUtil.method_363()) {
               this.field_303 = System.currentTimeMillis();
            }

            float var2 = this.field_2275.vSpeed.getValue();
            if (this.field_2275.accelerate.getValue()) {
               this.field_1570 = (double)(
                  (float)MovementUtil.method_2(
                     (double)this.field_2275.speed2.getValue().floatValue(),
                     (double)this.field_2275.accelMin.getValue().floatValue(),
                     (double)this.field_2275.accelTime.getValue().floatValue(),
                     this.field_303
                  )
               );
               var2 = (float)MovementUtil.method_2(
                  (double)var2,
                  (double)this.field_2275.verAccelMin.getValue().floatValue(),
                  (double)this.field_2275.accelTime.getValue().floatValue(),
                  this.field_2919
               );
            } else {
               this.field_1570 = (double)this.field_2275.speed2.getValue().floatValue();
            }

            this.field_1570 = this.field_1570 + (double)fireworks.method_100(false);
            if (this.field_31 == 0) {
               if (mc.player.input.keyPresses.jump()
                  && this.field_2275.vertical.getValue() == ElytraVerticalMode.STRICT
                  && this.field_1570 >= 2.0) {
                  this.field_1570 = 2.0;
               }

               double[] var3 = MovementUtil.method_2(var1, this.field_1570);
               mc.player.setDeltaMovement(var3[0], mc.player.getDeltaMovement().y, var3[1]);
               this.field_2918 = mc.player.getDeltaMovement();
               if (this.field_2275.antiKick.getValue() && mc.player.tickCount % 4 == 0) {
                  var1.setY(var1.method_395() - 0.0001);
                  mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0.0, -0.0001, 0.0));
               }
            }

            if (mc.player.input.keyPresses.jump() && mc.player.isFallFlying()) {
               if (this.field_2275.vertical.getValue() == ElytraVerticalMode.STRICT) {
                  this.field_31++;
               }

               if (this.field_2275.vertical.getValue() == ElytraVerticalMode.PLAIN) {
                  var1.setY((double)var2);
                  mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, (double)var2));
               } else if (this.field_2275.vertical.getValue() == ElytraVerticalMode.STRICT && MovementUtil.method_363()) {
                  this.method_845();
               }
            } else if (mc.player.input.keyPresses.shift()) {
               this.field_31 = 0;
               var1.setY((double)(-var2));
               mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, (double)(-var2)));
            } else {
               this.field_31 = 0;
               var1.setY(0.0);
               mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, 0.0));
               if (!mc.player.verticalCollision && this.field_2275.glide.getValue() != 0.0F) {
                  mc.player.setDeltaMovement(mc.player.getDeltaMovement().add(0.0, (double)(-this.field_2275.glide.getValue()), 0.0));
                  var1.setY(mc.player.getDeltaMovement().y);
               }
            }
         }
      }
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_303 = System.currentTimeMillis();
      }
   }

   @Override
   public void method_2(PacketSendEvent var1) {
      if (!this.method_557()) {
         if (var1.method_127() instanceof ServerboundMovePlayerPacket var2 && mc.player.isFallFlying()) {
            if (this.field_2275.method_1182() || !this.method_847() || !this.field_2275.method_558()) {
               return;
            }

            if (this.method_846()) {
               ((DuckPlayerMoveC2SPacket)(Object)var2).setPitch(this.method_501());
               return;
            }

            if (!MovementUtil.method_363()) {
               return;
            }

            if (this.field_2275.spoofPitch.getValue()) {
               ((DuckPlayerMoveC2SPacket)(Object)var2).setPitch(this.field_2275.pitch.getValue());
            }

            if (this.field_31 > 0) {
               ((DuckPlayerMoveC2SPacket)(Object)var2).setPitch(-this.field_2275.vPitch.getValue());
            }
         }
      }
   }

   @Override
   public void method_9(Event_19 var1) {
      if (mc.player.isFallFlying()
         && !this.field_2275.method_1182()
         && this.method_847()
         && this.field_2275.method_558()
         && var1.method_213() == PreType.PRE) {
         if (!this.method_557()) {
            if (this.method_846()) {
               var1.setPitch(this.method_501());
            } else if (MovementUtil.method_363()) {
               var1.setYaw(this.method_500());
               if (this.field_2275.spoofPitch.getValue()) {
                  var1.setPitch(this.field_2275.pitch.getValue());
               }

               if (this.field_31 > 0) {
                  var1.setPitch(-this.field_2275.vPitch.getValue());
               }
            }
         }
      }
   }

   @Override
   public void method_2(PostTickEvent var1) {
   }

   @Override
   public void method_9(TravelEvent var1) {
      if (this.field_2275.method_558() && mc.player.isFallFlying() && !this.field_2275.method_1182() && this.method_847()) {
         if (!this.method_557()) {
            if (this.method_846()) {
               var1.setPitch(this.method_501());
            }

            if (MovementUtil.method_363()) {
               var1.setYaw(this.method_500());
            }
         }
      }
   }

   @Override
   public void method_5(MovementInputEvent var1) {
      if (var1.method_276() != null && mc.player.input != null) {
         if (!this.method_557() && this.field_2275.method_1182()) {
            Inputs.setJump(var1.method_276(), true);
            Inputs.setForward(var1.method_276(), true);
            var1.method_276().moveVector = new Vec2(var1.method_276().moveVector.x, var1.method_278() ? var1.method_277() : 1F);
         }
      }
   }

   public void method_845() {
      double var1 = (double)MathUtil.method_7(-this.field_2275.vPitch.getValue());
      Vec3 var3 = this.field_2918;
      Vec3 var4 = MathUtil.method_9(-this.field_2275.vPitch.getValue(), mc.player.getYRot());
      double var5 = var3.horizontalDistance();
      double var7 = var4.horizontalDistance();
      double var9 = var4.length();
      double var11 = Math.cos(var1);
      var11 = var11 * var11 * Math.min(1.0, var9 / 0.4);
      var3 = var3.add(
         0.0,
         0.08
            * (-1.0 + var11 * 0.75),
         0.0
      );
      if (var1 < 0.0 && var7 > 0.0) {
         double var13 = var5 * -Math.sin(var1) * 0.04;
         var3 = var3.add(-var4.x * var13 / var7, var13 * 3.2, -var4.z * var13 / var7);
      }

      mc.player
         .setDeltaMovement(
            var3.multiply(
               0.99, 0.98, 0.99
            )
         );
      this.field_2918 = mc.player.getDeltaMovement();
   }

   public float method_500() {
      double[] var1 = MovementUtil.method_2(mc.player.getYRot(), mc.player.input, 1.0);
      return (float)(Math.toDegrees(Math.atan2(var1[1], var1[0])) - (double)Class_0245.field_685);
   }

   public float method_501() {
      float var1 = mc.player.getXRot();
      if (this.method_846()) {
         var1 = (float)Class_0245.field_685;
         if (MovementUtil.method_363()) {
            var1 = (float)Class_0245.field_684;
         }

         if (mc.player.input.keyPresses.jump()) {
            var1 = -var1;
         }
      }

      return var1;
   }

   public boolean method_846() {
      return mc.player.input.keyPresses.jump()
         || mc.player.input.keyPresses.shift() && this.field_2275.vertical.getValue() == ElytraVerticalMode.PLAIN && RotationManager.method_513();
   }

   public boolean method_847() {
      if (!mc.player.isFallFlying() || !this.field_2275.method_558() || !fireworks.autoLaunch.getValue() || !fireworks.isToggled()) {
         return true;
      } else {
         return !fireworks.field_2691.method_9(500L) ? true : fireworks.method_784();
      }
   }

   public boolean method_557() {
      return field_2917 == null ? false : field_2917.method_1088();
   }
}
