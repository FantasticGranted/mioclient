package me.mioclient.internal;

import java.util.LinkedList;
import java.util.Queue;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.WarpRechargeMode;
import me.mioclient.enum_.WarpMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.movement.WarpModule;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerboundPongPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public class Class_1359 implements MinecraftAccess {
   public final Queue<Packet<?>> field_4427 = new LinkedList<>();
   public final WarpModule field_4428;

   public Class_1359(WarpModule var1) {
      super();
      lookups.method_14(this);
      this.field_4428 = var1;
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.field_4428.method_363() && this.field_4428.mode.getValue() == WarpMode.PLAIN) {
         this.field_4428.field_1119 = Math.min(this.field_4428.charge.getValue(), this.field_4428.field_1119 + this.field_4428.chargeSpeed.getValue());
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (!this.method_535()) {
         if (this.field_4428.isToggled()
            && this.field_4428.mode.getValue() == WarpMode.ALTERNATIVE
            && var1.method_127() instanceof ServerboundPongPacket var2) {
            if (!this.field_4428.method_385() && this.field_4427.size() < this.field_4428.charge.getValue()) {
               var1.method_463();
               this.field_4427.add(var2);
            }

            if (!this.field_4427.isEmpty()) {
               var1.method_463();
            }
         }

         if (this.field_4428.mode.getValue() == WarpMode.PLAIN && var1.method_127() instanceof ServerboundMovePlayerPacket) {
            if (!this.field_4428.method_363()
               || this.field_4428.field_1119 == 0
               || !this.field_4428.isToggled() && this.field_4428.recharge.getValue() == WarpRechargeMode.HOLD) {
               return;
            }

            this.field_4428.field_1119 = Math.max(0, this.field_4428.field_1119 - 1);
         }
      }
   }
}
