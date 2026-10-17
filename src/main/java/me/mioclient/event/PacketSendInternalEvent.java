package me.mioclient.event;

import me.mioclient.internal.PacketEvent;
import net.minecraft.network.protocol.Packet;

/** Fired when a packet is sent through the internal path. */
public final class PacketSendInternalEvent extends PacketEvent {
   public PacketSendInternalEvent(Packet<?> var1) {
      super(var1);
   }
}
