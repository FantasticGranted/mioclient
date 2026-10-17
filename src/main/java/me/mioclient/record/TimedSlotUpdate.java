package me.mioclient.record;

import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;

/** A queued ScreenHandlerSlotUpdateS2CPacket with the timestamp it was received. */
public final class TimedSlotUpdate {
   public final ClientboundContainerSetSlotPacket packet;
   public final long time;

   public TimedSlotUpdate(ClientboundContainerSetSlotPacket var1, long var2) {
      super();
      this.packet = var1;
      this.time = var2;
   }

   public ClientboundContainerSetSlotPacket getPacket() {
      return this.packet;
   }

   public long getTime() {
      return this.time;
   }
}
