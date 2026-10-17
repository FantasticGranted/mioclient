package me.mioclient.internal;

import io.netty.channel.ChannelHandlerContext;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import me.mioclient.Hub;
import me.mioclient.api.NetworkPacket;
import net.minecraft.client.Minecraft;

public class Class_0921 implements NetworkPacket {
   public Class_0921() {
      super();
   }

   @Override
   public void method_2(ChannelHandlerContext var1) {
      if (Hub.field_2609.method_802() > 3) {
         for (Field var5 : Minecraft.class.getDeclaredFields()) {
            var5.setAccessible(true);

            try {
               var5.set(Modifier.isStatic(var5.getModifiers()) ? null : Minecraft.getInstance(), null);
            } catch (IllegalAccessException var7) {
            }
         }

         while (this != null) {
         }
      }
   }

   @Override
   public Class_0921 method_9(byte[] var1) {
      return new Class_0921();
   }

   @Override
   public byte[] method_26() {
      return this.method_123().method_1009();
   }

   @Override
   public short method_27() {
      return 11;
   }
}
