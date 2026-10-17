package me.mioclient.internal;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import me.mioclient.api.NetworkPacket;

/** Netty encoder that serializes a custom NetworkPacket to bytes. */
public class PacketEncoder extends MessageToByteEncoder<NetworkPacket> {
   public PacketEncoder() {
      super();
   }

   @Override
   public void encode(ChannelHandlerContext var1, NetworkPacket var2, ByteBuf var3) {
      byte[] var4 = Class_0338.method_37(var2.method_26());
      var1.writeAndFlush(Unpooled.copiedBuffer(var4));
   }
}
