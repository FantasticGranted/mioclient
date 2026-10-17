package me.mioclient.internal;

import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.enum_.TakeoffMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.movement.ElytraFlyModule;
import net.minecraft.network.protocol.game.ClientboundSetHealthPacket;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import me.mioclient.internal.Inputs;

public class Class_0613 extends ElytraFlyBehavior {
   public static final AbstractModule_28 field_1894 = Hub.field_2595.method_78(AbstractModule_28.class);
   public final Timer field_1895 = new Timer();
   public boolean field_1896;
   public boolean field_1897;
   public float field_1761;

   public Class_0613(ElytraFlyModule var1) {
      super(var1);
   }

   @Override
   public void onEnable() {
      this.field_1896 = true;
      this.field_1761 = this.field_2275.boostPitch.getValue();
   }

   @Override
   public void method_2(PlayerTickEvent var1) {
   }

   @Override
   public void method_2(MoveEvent var1) {
      float var2 = this.field_2275.limit.getValue();
      if (var2 > 2e+02F && this.method_625()) {
         var2 = 2e+02F;
      }

      if (!this.field_2275.limit.method_84() || this.method_625()) {
         Vec3 var3 = var1.method_1066();
         double var4 = Math.hypot(var3.x(), var3.z());
         if (!(var4 * 2e+01 * 3.6 <= (double)var2)) {
            Vec3 var6 = var1.method_1066().normalize();
            var6 = var6.scale((double)var2 / 3.6 / 2e+01);
            var1.method_7(var6.x(), var6.z());
         }
      }
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundSetHealthPacket var2 && var2.getHealth() + var2.getSaturation() < EntityUtil.method_76()) {
         this.onDisable();
         this.onEnable();
      }
   }

   @Override
   public void method_2(PacketSendEvent var1) {
   }

   @Override
   public void method_9(Event_19 var1) {
   }

   @Override
   public void method_2(PostTickEvent var1) {
      this.field_1897 = false;
      if (!PlayerUtil.method_7(mc.player)) {
         if (!field_1894.method_1052() || !mc.player.isUsingItem()) {
            boolean var2 = mc.player.input.keyPresses.jump();
            if (this.field_2275.takeoff.getValue() == TakeoffMode.STRICT) {
               var2 = !mc.player.onGround() && mc.player.getDeltaMovement().y() < 0.1;
            } else {
               Hub.field_2596.method_38(this.field_2275);
            }

            if (mc.player.isFallFlying()) {
               this.field_1895.reset();
            }

            if ((var2 || !this.field_1895.method_9(1500L))
               && !mc.player.isFallFlying()
               && !EntityUtil.method_2(mc.player)
               && this.field_2275.method_558()) {
               PacketSender.method_1099();
               mc.player.startFallFlying();
            }

            if (this.field_2275.method_428()) {
               if (this.field_2275.autoBoost.getValue()) {
                  this.method_626();
               }

               float var3 = (float)Math.toRadians((double)mc.player.getYRot());
               float var4 = this.field_2275.minBoost.getValue() * 0.1F;
               if (this.field_2275.minBoost.method_84() && field_1894.method_1052()) {
                  var4 = mc.player.getXRot() > 0.0F
                     ? 0.025F
                     : 0.018F + (float)this.field_2275.verticalBoost.getValue().intValue() * 0.001F;
                  if (mc.player.getXRot() <= 0.0F
                     && !Hub.field_2602.method_984().method_2(1e+01, TimeUnit.SECONDS)) {
                     var4 = 0.008F;
                  }
               }

               if (this.method_625()) {
                  var4 = 3F;
               }

               this.method_624();
               if (!this.field_1897) {
                  if (this.field_2275.inLava.getValue() && mc.player.isInLava()) {
                     var4 = 0.2F * this.field_2275.speed.getValue();
                  }

                  if (this.field_2275.factorize.getValue()
                     ? !(mc.player.getXRot() > 0.0F)
                     : !mc.options.keyUp.isDown() && !this.field_2275.always.getValue()) {
                     if (mc.options.keyDown.isDown()) {
                        mc.player.push((double)(Mth.sin(var3) * var4), 0.0, (double)(Mth.cos(var3) * -var4));
                     }
                  } else {
                     mc.player.push((double)(Mth.sin(var3) * -var4), 0.0, (double)(Mth.cos(var3) * var4));
                  }
               }
            }
         }
      }
   }

   @Override
   public void method_9(TravelEvent var1) {
   }

   @Override
   public void method_5(MovementInputEvent var1) {
      if (this.field_2275.autoJump.getValue() && mc.player.isFallFlying()) {
         Inputs.setJump(var1.method_276(), true);
      }
   }

   public void method_624() {
      if (mc.player.getXRot() < 0.0F && this.field_2275.nCPBoost.getValue()) {
         int var1 = mc.player.getFallFlyingTicks();
         if (var1 % 6 > 2 || var1 >= 1 && var1 < 10) {
            this.field_1897 = true;
            return;
         }

         Hub.field_2598.method_2(new float[]{mc.player.getYRot(), 0.0F}, 10000, true);
         Hub.field_2598.method_511();
      }
   }

   public boolean method_625() {
      return this.field_2275.nCPBoost.getValue() && mc.player.getFallFlyingTicks() <= 5;
   }

   public void method_626() {
      float var1 = this.field_2275.boostPitch.getValue();
      if (mc.player.getY() >= (double)(this.field_2275.minY.getValue() + this.field_2275.add.getValue())) {
         this.field_1896 = true;
      }

      if (mc.player.getY() <= (double)this.field_2275.minY.getValue().intValue() && this.field_1896) {
         this.field_1896 = false;
      }

      if (this.field_1896) {
         this.field_1761 = this.field_1761 + 4F;
      } else {
         this.field_1761 = this.field_1761 - 4F;
      }

      this.field_1761 = Mth.clamp(this.field_1761, -var1, Math.max(var1, mc.player.getXRot()));
   }

   public float method_501() {
      return this.field_1761;
   }
}
