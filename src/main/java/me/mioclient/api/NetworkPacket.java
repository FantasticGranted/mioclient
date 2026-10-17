package me.mioclient.api;

import io.netty.channel.ChannelHandlerContext;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import me.mioclient.internal.PacketWriter;

/** A custom mod network packet: id, encode/decode, and channel handling. */
public interface NetworkPacket {
   void method_2(ChannelHandlerContext var1);

   NetworkPacket method_9(byte[] var1);

   byte[] method_26();

   short method_27();

   default DataInputStream method_5(byte[] var1) {
      ByteArrayInputStream var2 = new ByteArrayInputStream(var1);
      DataInputStream var3 = new DataInputStream(var2);
      try { var3.readShort(); } catch (java.io.IOException e) { throw new RuntimeException(e); }
      return var3;
   }

   default PacketWriter method_123() {
      PacketWriter var1 = new PacketWriter(new ByteArrayOutputStream());
      try { var1.writeShort(this.method_27()); } catch (java.io.IOException e) { throw new RuntimeException(e); }
      return var1;
   }
}
