package me.mioclient.event;

import me.mioclient.internal.PacketEvent;
import net.minecraft.network.protocol.Packet;

/** Fired when an inbound packet is received. */
public final class PacketReceiveEvent extends PacketEvent {
   public PacketReceiveEvent(Packet<?> var1) {
      super(var1);
   }
}
