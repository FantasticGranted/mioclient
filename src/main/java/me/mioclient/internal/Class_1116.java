package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;

public final class Class_1116 {
   public static final long field_3455 = 750L;
   public final SpeedMineModule field_3456;
   public final Timer field_3457 = new Timer();
   public boolean field_3458;
   public BlockPos field_355;

   public Class_1116(SpeedMineModule var1) {
      super();
      this.field_3456 = var1;
   }

   public void method_7(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundBlockUpdatePacket var2 && var2.getBlockState().isAir() && var2.getPos().equals(this.field_355)) {
         this.reset();
      }

      if (var1.method_127() instanceof ClientboundSectionBlocksUpdatePacket var4) {
         var4.runUpdates((var1x, var2x) -> {
            if (var1x.equals(this.field_355) && var2x.isAir()) {
               this.reset();
            }
         });
      }
   }

   public void method_994(BlockPos var1) {
      if (!this.field_3458) {
         this.field_3458 = true;
         this.field_355 = var1;
         this.field_3457.reset();
      }
   }

   public void method_142() {
      if (this.field_3458) {
         if (!BlockUtil.method_3(this.field_355) || BlockUtil.method_14(this.field_355)) {
            this.reset();
         }

         if (this.field_355 != null && !this.field_355.equals(this.field_3456.method_1120())) {
            this.reset();
         }
      }
   }

   public boolean method_995() {
      if (this.field_3458 && this.field_355 != null) {
         long var1 = (long)(
            Math.ceil((double)((float)Hub.field_2602.method_983() * 15F / 5e+01F))
               * 5e+01
         );
         var1 = Math.max(var1, 750L);
         if (this.field_3456.tPSSync.getValue()) {
            var1 = (long)((float)var1 / Hub.field_2602.method_990());
         }

         var1 = (long)((float)var1 + this.field_3456.instantDelay.getValue());
         return this.field_3457.method_9(var1);
      } else {
         return false;
      }
   }

   public void reset() {
      this.field_3458 = false;
      this.field_355 = null;
   }
}
