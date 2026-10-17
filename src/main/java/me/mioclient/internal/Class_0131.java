package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.module.movement.EntityControlModule;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ServerboundPaddleBoatPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public final class Class_0131 implements MinecraftAccess {
   public final Timer field_393 = new Timer();
   public final EntityControlModule field_394;
   public int field_395;
   public int field_396;
   public boolean field_397 = true;
   public Vec3 field_398;

   public Class_0131(EntityControlModule var1) {
      super();
      this.field_394 = var1;
   }

   public boolean method_155() {
      if (!mc.player.isPassenger()) {
         return true;
      } else {
         long var1 = 1000L;
         if (this.field_395 >= 3) {
            var1 = 250L;
         }

         if (this.field_393.method_9(var1)) {
            this.method_156();
         }

         Vec3 var3 = mc.player.getVehicle().position();
         if (this.field_396 > 0) {
            this.field_396--;
         }

         boolean var4 = false;
         if (this.field_398 == null || var4 || Math.abs(var3.y - this.field_398.y) >= 18.0) {
            this.method_156();
            this.field_398 = var3;
         }

         return this.field_397;
      }
   }

   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundMovePlayerPacket || var1.method_127() instanceof ServerboundPaddleBoatPacket) {
         var1.method_463();
      }
   }

   public void method_2(Class_0542 var1) {
      this.field_397 = true;
      Entity var2 = mc.player.getVehicle();
      if (var2 != null) {
         Vec3 var3 = var1.method_384();
         var2.absSnapTo(var3.x(), var3.y(), var3.z());
         this.method_157(0.0F);
         var1.method_463();
         this.field_396 = 2;
      }
   }

   public void method_156() {
      Entity var1 = mc.player.getVehicle();
      if (var1 != null) {
         if (this.field_398 != null && this.field_398.y() < var1.getY()) {
            this.field_395++;
         } else {
            this.field_395 = 0;
         }

         this.method_157(1e+03F);
         this.field_393.reset();
         this.field_397 = false;
      }
   }

   public void method_157(float var1) {
      Entity var2 = mc.player.getVehicle();
      if (var2 != null) {
         ServerboundMoveVehiclePacket var3 = ServerboundMoveVehiclePacket.fromEntity(var2);
         var3 = new ServerboundMoveVehiclePacket(var3.position().add(0.0, (double)var1, 0.0), var3.yRot(), var3.xRot(), var3.onGround());
         PacketSender.method_2(var3);
      }
   }

   public boolean method_158(int var1) {
      return this.field_395 >= var1;
   }
}
