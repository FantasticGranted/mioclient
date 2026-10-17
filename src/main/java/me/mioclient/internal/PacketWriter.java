package me.mioclient.internal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/** DataOutputStream backed by a ByteArrayOutputStream, exposing the written bytes. */
public class PacketWriter extends DataOutputStream {
   public final ByteArrayOutputStream field_3806;

   public PacketWriter(ByteArrayOutputStream var1) {
      super(var1);
      this.field_3806 = var1;
   }

   public byte[] method_1009() {
      return this.field_3806.toByteArray();
   }
}
