package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_1054;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.abstract_.AbstractModule_28;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public final class Class_0910 implements MinecraftAccess {
   public final AbstractModule_28 field_2886;
   public boolean field_821;

   public Class_0910(AbstractModule_28 var1) {
      super();
      this.field_2886 = var1;
   }

   public void method_40() {
      if (this.method_295()) {
         if (this.field_2886.rotations.getValue() == Class_1054.SILENT) {
            PacketSender.method_2(
               mc.player.getX(),
               mc.player.getY(),
               mc.player.getZ(),
               mc.player.getYRot(),
               mc.player.getXRot(),
               mc.player.onGround()
            );
         }

         this.method_221(true);
      }
   }

   public void method_9(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket var2 && this.method_2(var2)) {
         float var4 = var2.getYRot(0.0F) + 3.6e+10F;
         ((DuckPlayerMoveC2SPacket)(Object)var2).setYaw(var4);
      }
   }

   public boolean method_295() {
      return this.method_2(null);
   }

   public boolean method_2(ServerboundMovePlayerPacket var1) {
      if (!MovementUtil.method_363() && Hub.field_2614.method_874() < 5.0) {
         return false;
      } else if (!this.field_2886.field_3742.getValue()) {
         return false;
      } else if (PlayerUtil.method_428()) {
         return false;
      } else {
         if (var1 != null) {
            boolean var2 = mc.screen instanceof AbstractContainerScreen && mc.player.containerMenu == mc.player.inventoryMenu;
            if (!var1.hasRotation() && !var2) {
               return false;
            }
         }

         return this.field_2886.field_3749 || var1 == null;
      }
   }

   public boolean method_826() {
      return this.field_821;
   }

   public void method_221(boolean var1) {
      this.field_821 = var1;
   }
}
