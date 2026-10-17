package me.mioclient.internal;

import io.netty.channel.ChannelHandlerContext;
import java.awt.Color;
import java.io.DataInputStream;
import me.mioclient.Hub;
import me.mioclient.api.NetworkPacket;
import me.mioclient.module.client.IRCModule;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;

public class Class_1325 implements NetworkPacket {
   public static IRCModule field_906 = Hub.field_2595.method_78(IRCModule.class);
   public String field_4278;
   public String field_4279;
   public String field_483;

   public Class_1325() {
      super();
   }

   @Override
   public void method_2(ChannelHandlerContext var1) {
      if (field_906.chat.getValue()) {
         if (Minecraft.getInstance().player != null
            && Minecraft.getInstance().level != null
            && !Hub.field_2610.method_331().contains(this.field_4279.toLowerCase())) {
            field_906.field_576++;
            MutableComponent var2 = Component.empty();
            var2.append("<");
            Color var3 = field_906.gradient.getValue()
               ? Colors.method_2(Colors.method_959(), Colors.method_959().darker(), 3e+03, 0.0)
               : Colors.method_959();
            var2.append(Component.literal(this.field_4278).withStyle(var1x -> ChatUtil.method_2(var1x, var3::hashCode)));
            var2.append("> ");
            var2.append(this.field_483);
            String var10000 = this.field_483;
            String var10001 = this.field_4278;
            int var10002 = field_906.field_576;
            MessageSignature var4 = ChatUtil.method_38(
               new PlaceholderText().method_2(var10002).method_2((Object)var10001).method_2((Object)var10000).method_9("\u0001\u0001\u0001").hashCode()
            );
            ChatUtil.method_2(var2, var4);
            if (field_906.chatSound.getValue()
               && !this.field_4278.contains(Hub.field_2609.method_801())
               && !this.field_4278.contains(Minecraft.getInstance().getUser().getName())) {
               Hub.field_2606.method_2(field_906.mode.getValue()).method_230(field_906.volume.getValue());
            }
         }
      }
   }

   @Override
   public Class_1325 method_9(byte[] var1) {
      DataInputStream var2 = this.method_5(var1);
      Class_1325 var3 = new Class_1325();
      try {
var3.field_4278 = var2.readUTF();
      var3.field_4279 = var2.readUTF();
      var3.field_483 = var2.readUTF();
      } catch (java.io.IOException e) { throw new RuntimeException(e); }
      return var3;
   }

   @Override
   public byte[] method_26() {
      PacketWriter var1 = this.method_123();
      try {
var1.writeUTF(this.field_4278);
      var1.writeUTF(this.field_4279);
      var1.writeUTF(this.field_483);
      } catch (java.io.IOException e) { throw new RuntimeException(e); }
      return var1.method_1009();
   }

   @Override
   public short method_27() {
      return 5;
   }
}
