package me.mioclient.internal;

import java.util.ArrayDeque;
import java.util.Queue;
import me.mioclient.Hub;
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
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerCombatKillPacket;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class Class_0517 extends ElytraFlyBehavior {
   public static final AbstractModule_6 field_1637 = Hub.field_2595.method_78(AbstractModule_6.class);
   public final Queue<Packet<?>> field_1638 = new ArrayDeque<>();
   public int field_1639 = 0;
   public boolean field_1640;

   public Class_0517(ElytraFlyModule var1) {
      super(var1);
   }

   @Override
   public void onDisable() {
      mc.options.keyJump.setDown(false);
      mc.player.setSprinting(false);
      this.field_1639 = 0;
      this.method_159();
   }

   @Override
   public void method_2(PlayerTickEvent var1) {
      if (!this.field_2275.method_1180() && !this.method_557()) {
         if (mc.player.isFallFlying()) {
            mc.player.avatarState().walkDist = 0.0F;
         }

         if (Hub.field_2615.method_1162() >= 40) {
            this.method_159();
         }

         if (!PlayerUtil.method_29(mc.player) && !PlayerUtil.method_5(mc.player)) {
            if (this.field_2275.compensate.getValue()) {
               this.field_1639--;
               if (this.field_1639 == 0) {
                  this.method_159();
               }
            } else if (this.field_1639 != 0) {
               this.field_1639 = 0;
               this.method_159();
            }
         } else {
            this.method_159();
         }
      }
   }

   @Override
   public void method_2(MoveEvent var1) {
   }

   @Override
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerCombatKillPacket || var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.method_159();
      }
   }

   @Override
   public void method_2(PacketSendEvent var1) {
      if (!this.method_557()) {
         if (var1.method_127() instanceof DuckPlayerMoveC2SPacket var2
            && this.method_558()
            && this.field_2275.silent.getValue()
            && this.field_2275.pitchLock.getValue()
            && PlayerUtil.method_428()
            && !this.field_2275.method_1180()) {
            var2.setPitch(this.field_2275.pitch2.getValue());
         }

         if (this.field_2275.compensate.getValue() && mc.player.isFallFlying() && this.method_558() && !this.field_2275.method_1180()) {
            if (!(var1.method_127() instanceof ServerboundPlayerCommandPacket) && !(var1.method_127() instanceof ServerboundPongPacket)) {
               this.field_1638.add(var1.method_127());
               var1.method_463();
            }
         }
      }
   }

   @Override
   public void method_9(Event_19 var1) {
      if (!this.field_2275.method_1180() && this.method_558() && !this.method_557()) {
         if (this.field_2275.pitchLock.getValue() && this.field_2275.silent.getValue()) {
            var1.setPitch(this.field_2275.pitch2.getValue());
         }
      }
   }

   @Override
   public void method_2(PostTickEvent var1) {
      this.field_1640 = this.field_2275.method_1183();
      if (mc.player.isFallFlying() && Hub.field_2615.method_1161() > 5) {
         mc.player.stopFallFlying();
      } else {
         boolean var2 = this.method_558();
         if (!PlayerUtil.method_29(mc.player)
            && !PlayerUtil.method_5(mc.player)
            && !this.field_2275.method_1180()
            && !this.method_557()
            && var2) {
            if (!PlayerUtil.method_428() && Hub.field_2615.method_1161() < 3 && !this.field_1640) {
               mc.player.startFallFlying();
               PacketSender.method_1099();
            }

            if (this.field_2275.infDurability.getValue() && !this.field_1640) {
               mc.gameMode.handleContainerInput(0, 6, 0, ContainerInput.PICKUP, mc.player);
               mc.gameMode.handleContainerInput(0, 6, 0, ContainerInput.PICKUP, mc.player);
               PacketSender.method_1099();
            }

            if (!mc.player.horizontalCollision) {
               if (MovementUtil.method_363()) {
                  mc.player.setSprinting(true);
                  if (mc.player.isFallFlying()) {
                     mc.player.setShiftKeyDown(false);
                  }
               }

               if (mc.player.onGround() && this.field_2275.compensate.getValue()) {
                  this.field_1639 = 3;
               }
            }

            if (this.field_2275.pitchLock.getValue() && !this.field_2275.silent.getValue()) {
               mc.player.setXRot(this.field_2275.pitch2.getValue());
            }
         }
      }
   }

   @Override
   public void method_9(TravelEvent var1) {
      if (this.method_558() && !this.method_557()) {
         if (this.field_2275.pitchLock.getValue() && !this.field_2275.method_1180()) {
            var1.setPitch(this.field_2275.pitch2.getValue());
            var1.method_463();
         }
      }
   }

   @Override
   public void method_5(MovementInputEvent var1) {
      if (!this.method_557()) {
         if (this.method_558() && PlayerUtil.method_428() && !this.field_2275.method_1180()) {
            Inputs.setJump(var1.method_276(), true);
            Inputs.setForward(var1.method_276(), true);
            var1.method_276().moveVector = new Vec2(var1.method_276().moveVector.x, var1.method_278() ? var1.method_277() : 1F);
            mc.player.setSprinting(true);
         }
      }
   }

   public void method_159() {
      if (this.method_535()) {
         this.field_1638.clear();
      } else {
         while (!this.field_1638.isEmpty()) {
            Packet var1 = this.field_1638.poll();
            if (var1 == null) {
               break;
            }

            PacketSender.method_9(var1);
         }
      }
   }

   public boolean method_557() {
      return field_1637 == null ? false : field_1637.method_639();
   }

   public boolean method_558() {
      return this.field_2275.method_558() || this.field_1640;
   }
}
