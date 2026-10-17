package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.ServerConnectEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.ServerData.Type;
import net.minecraft.client.multiplayer.TransferState;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ConnectScreen.class})
public class MixinConnectScreen {
   public MixinConnectScreen() {
      super();
   }

   @Inject(
      method = {"connect(Lnet/minecraft/client/Minecraft;Lnet/minecraft/client/multiplayer/resolver/ServerAddress;Lnet/minecraft/client/multiplayer/ServerData;Lnet/minecraft/client/multiplayer/TransferState;)V"},
      at = {@At("HEAD")}
   )
   private void connectHook(Minecraft var1, ServerAddress var2, ServerData var3, TransferState var4, CallbackInfo var5) {
      ServerData var6 = var3;
      if (var3 == null) {
         var6 = new ServerData("mioclient", var2.getHost(), Type.OTHER);
      }

      MinecraftAccess.lookups.method_36(new ServerConnectEvent(var6.ip));
      Hub.field_2602.method_2(var6);
   }
}
