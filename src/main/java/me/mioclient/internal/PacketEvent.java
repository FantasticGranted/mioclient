package me.mioclient.internal;

import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.PacketSendInternalEvent;
import net.minecraft.network.protocol.Packet;

/** Base event carrying a network packet (send/receive subtypes). */
public abstract class PacketEvent extends Event {
   public final Packet<?> field_332;

   public PacketEvent(Packet<?> var1) {
      super();
      this.field_332 = var1;
   }

   public Packet<?> method_127() {
      return this.field_332;
   }
}
