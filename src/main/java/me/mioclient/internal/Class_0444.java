package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.movement.HoleSnapModule;
import me.mioclient.module.movement.WarpModule;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;

public final class Class_0444 implements MinecraftAccess {
   public static boolean field_1422;
   public static WarpModule field_1423 = Hub.field_2595.method_78(WarpModule.class);
   public final HoleSnapModule field_1424;
   public int field_31;

   public Class_0444(HoleSnapModule var1) {
      super();
      this.field_1424 = var1;
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_363()) {
         this.field_31 = Math.min(field_1423.charge.getValue(), ++this.field_31);
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (this.method_363() && this.field_31 != 0) {
         if (var1.method_127() instanceof ServerboundMovePlayerPacket && field_1422 || field_1423.isToggled()) {
            this.field_31 = Math.max(0, --this.field_31);
         }
      }
   }

   public boolean method_484() {
      if (this.field_31 > 0 && this.method_363() && this.field_1424.shift.getValue() && mc.player.onGround() && Hub.field_2615.method_1161() > 1
         )
       {
         Hub.field_2617.method_2(this.field_1424, (float)field_1423.boost.getValue().intValue());
         return true;
      } else {
         return false;
      }
   }

   public boolean method_363() {
      return mc.player.position().distanceToSqr(mc.player.xo, mc.player.yo, mc.player.zo) > 0.0;
   }
}
