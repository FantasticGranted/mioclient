package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.RemovalMode;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.Event_50;
import me.mioclient.event.ServerSlotUpdateEvent;
import me.mioclient.mixin.ducks.DuckExplosionS2CPacket;
import me.mioclient.module.movement.VelocityModule;
import me.mioclient.module.render.NoRenderModule;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundSectionBlocksUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.network.protocol.game.ClientboundSetHeldSlotPacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPacketListener.class})
public class MixinClientPlayNetworkHandler {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static VelocityModule velocity = Hub.field_2595.method_78(VelocityModule.class);

   public MixinClientPlayNetworkHandler() {
      super();
   }

   @Inject(
      method = {"handleExplosion"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void onExplosionVelocity(ClientboundExplodePacket var1, CallbackInfo var2) {
      ExplosionEvent var3 = new ExplosionEvent(var1);
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      } else {
          DuckExplosionS2CPacket var4 = (DuckExplosionS2CPacket)(Object)var1;
          var4.setPlayerKnockback(Optional.of(new Vec3(var3.method_398(), var3.method_399(), var3.method_400())));
      }
   }

   @Inject(
      method = {"handleSetEntityMotion"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void onEntityVelocityUpdate(ClientboundSetEntityMotionPacket var1, CallbackInfo var2) {
      if (velocity.method_734() && var1.id() == Minecraft.getInstance().player.getId()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"handleAddEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onEntitySpawn(ClientboundAddEntityPacket var1, CallbackInfo var2) {
      if (norender.isToggled()) {
         if (var1 != null && var1.getType() != null && norender.method_2(var1.getType()) && norender.removal.getValue() == RemovalMode.FULL) {
            var2.cancel();
         }
      }
   }

   @Inject(
      method = {"handleSetHeldSlot"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void onUpdateSelectedSlot(ClientboundSetHeldSlotPacket var1, CallbackInfo var2) {
      ServerSlotUpdateEvent var3 = new ServerSlotUpdateEvent(var1.slot());
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"handleBlockUpdate"},
      at = {@At("TAIL")}
   )
   private void onBlockUpdateHook(ClientboundBlockUpdatePacket var1, CallbackInfo var2) {
      MinecraftAccess.lookups.method_36(new Event_50());
   }

   @Inject(
      method = {"handleChunkBlocksUpdate"},
      at = {@At("TAIL")}
   )
   private void onChunkDeltaUpdateHook(ClientboundSectionBlocksUpdatePacket var1, CallbackInfo var2) {
      MinecraftAccess.lookups.method_36(new Event_50());
   }
}
