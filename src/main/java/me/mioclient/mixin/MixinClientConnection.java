package me.mioclient.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.PacketSendInternalEvent;
import me.mioclient.internal.ChatUtil;
import me.mioclient.module.misc.NoPacketKickModule;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketSendListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.PacketFlow;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Connection.class})
public class MixinClientConnection {
   private static NoPacketKickModule nopacketkick = Hub.field_2595.method_78(NoPacketKickModule.class);
   @Shadow
   private Channel channel;
   @Shadow
   @Final
   private PacketFlow receiving;

   public MixinClientConnection() {
      super();
   }

   @Inject(
      method = {"channelRead0*"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void channelRead0(ChannelHandlerContext var1, Packet<?> var2, CallbackInfo var3) {
      if (this.channel.isOpen() && var2 != null) {
         try {
            PacketReceiveEvent var4 = new PacketReceiveEvent(var2);
            MinecraftAccess.lookups.method_36(var4);
            if (var4.method_464()) {
               var3.cancel();
            }
         } catch (Exception var5) {
         }
      }
   }

   @Inject(
      method = {"sendPacket"},
      at = {@At("HEAD")},
      cancellable = true
   )
    private void sendImmediately(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (this.receiving == PacketFlow.CLIENTBOUND) {
         try {
            PacketSendEvent var5 = new PacketSendEvent(var1);
            MinecraftAccess.lookups.method_36(var5);
            if (var5.method_464()) {
               var4.cancel();
            }
         } catch (Exception var6) {
         }
      }
   }

   @Inject(
      method = {"doSendPacket"},
      at = {@At("TAIL")}
   )
    private void sendInternal(Packet<?> var1, ChannelFutureListener var2, boolean var3, CallbackInfo var4) {
      if (this.receiving == PacketFlow.CLIENTBOUND) {
         try {
            PacketSendInternalEvent var5 = new PacketSendInternalEvent(var1);
            MinecraftAccess.lookups.method_36(var5);
         } catch (Exception var6) {
         }
      }
   }

   @Inject(
      method = {"exceptionCaught"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void exceptionCaught(ChannelHandlerContext var1, Throwable var2, CallbackInfo var3) {
      if (nopacketkick.isToggled()) {
         if (nopacketkick.message.getValue()) {
            try {
               ChatUtil.method_2(nopacketkick.method_38(var2), ChatUtil.method_2(nopacketkick), Priority.HIGH);
            } catch (Throwable var5) {
            }
         }

         var3.cancel();
      }
   }
}
