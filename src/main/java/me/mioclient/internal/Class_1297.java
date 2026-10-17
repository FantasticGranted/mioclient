package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendInternalEvent;
import me.mioclient.event.Subscribe;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.phys.Vec3;
import net.minecraft.network.protocol.game.ServerboundPlayerInputPacket;

public final class Class_1297 implements MinecraftAccess {
   public double field_608;
   public double field_609;
   public double field_2311;
   public boolean field_2314;
   public boolean field_4193;
   public boolean field_4194;
   public boolean field_2838;
   public int field_1014;
   public int field_4195;
   public float[] field_4196;

   public Class_1297() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_4194 = this.field_4193;
      this.field_4193 = mc.player.isFallFlying();
      if (!mc.player.onGround() && !this.field_2314) {
         this.field_1014 = 0;
         this.field_4195++;
      } else {
         this.field_4195 = 0;
         this.field_1014++;
      }
   }

   @Subscribe
   public void method_9(PacketSendInternalEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket var2) {
         this.field_608 = var2.getX(mc.player.getX());
         this.field_609 = var2.getY(mc.player.getY());
         this.field_2311 = var2.getZ(mc.player.getZ());
         this.field_2314 = var2.isOnGround();
         this.field_4196 = new float[]{var2.getYRot(mc.player.getYRot()), var2.getXRot(mc.player.getXRot())};
      }

      if (var1.method_127() instanceof ServerboundPlayerInputPacket var4) {
         this.field_2838 = var4.input().shift();
      }
   }

   public void method_22(Runnable var1) {
      boolean var2 = Hub.field_2615.method_1160();
      if (var2) {
         PacketSender.method_2(Inputs.shift(false));
         var1.run();
         PacketSender.method_2(Inputs.shift(true));
      } else {
         var1.run();
      }
   }

   public void method_3(Runnable var1) {
      boolean var2 = !Hub.field_2615.method_1160();
      if (var2) {
         PacketSender.method_2(Inputs.shift(true));
         var1.run();
         PacketSender.method_2(Inputs.shift(false));
      } else {
         var1.run();
      }
   }

   public double method_380() {
      return this.field_608;
   }

   public double method_395() {
      return this.field_609;
   }

   public double method_396() {
      return this.field_2311;
   }

   public Vec3 method_384() {
      return new Vec3(this.field_608, this.field_609, this.field_2311);
   }

   public boolean method_707() {
      return this.field_2314;
   }

   public boolean method_1159() {
      return this.field_4194;
   }

   public boolean method_1160() {
      return this.field_2838;
   }

   public int method_1161() {
      return this.field_1014;
   }

   public int method_1162() {
      return this.field_4195;
   }

   public float[] method_1163() {
      return this.field_4196;
   }
}
