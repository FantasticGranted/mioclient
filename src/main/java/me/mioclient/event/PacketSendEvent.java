package me.mioclient.event;

import me.mioclient.internal.PacketEvent;
import net.minecraft.network.protocol.Packet;

/** Fired when a packet is sent immediately. */
public final class PacketSendEvent extends PacketEvent {
   public PacketSendEvent(Packet<?> var1) {
      super(var1);
   }
}
