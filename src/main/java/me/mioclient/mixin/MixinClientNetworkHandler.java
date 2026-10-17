package me.mioclient.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.ChatReceiveEvent;
import me.mioclient.internal.Class_0542;
import me.mioclient.internal.CommandManager;
import me.mioclient.module.render.AmbienceModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundMoveVehiclePacket;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientPacketListener.class})
public class MixinClientNetworkHandler {
   private static final AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);
   @Shadow
   private ClientLevel level;
   @Unique
   private ChatReceiveEvent event;

   public MixinClientNetworkHandler() {
      super();
   }

   @Inject(
      method = {"sendChat"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void sendChatMessageHook(String var1, CallbackInfo var2) {
      if (var1.startsWith(CommandManager.method_927())) {
         CommandManager.method_7(var1.substring(CommandManager.method_927().length()));
         var2.cancel();
      }
   }

   @ModifyVariable(
      method = {"sendChat"},
      at = @At(
         value = "INVOKE",
         target = "Ljava/time/Instant;now()Ljava/time/Instant;",
         shift = Shift.BEFORE
      ),
      argsOnly = true
   )
   private String dabigbulletz(String var1) {
      this.event = new ChatReceiveEvent(var1);
      MinecraftAccess.lookups.method_36(this.event);
      return this.event.method_219();
   }

   @Inject(
      method = {"sendChat"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/time/Instant;now()Ljava/time/Instant;",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void dabigbulletz(String var1, CallbackInfo var2) {
      if (this.event != null && this.event.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"handleSetTime"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/network/protocol/PacketUtils;ensureRunningOnSameThread(Lnet/minecraft/network/protocol/Packet;Lnet/minecraft/network/PacketListener;Lnet/minecraft/network/PacketProcessor;)V",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void onWorldTimeUpdateHook(ClientboundSetTimePacket var1, CallbackInfo var2) {
      if (ambience.isToggled() && ambience.worldTime.getValue()) {
         this.level.getLevelData().setGameTime(ambience.method_94());
         // this.level.setDayTime(ambience.method_94()); // TODO(runtime): day-time setter removed in 26.1
         var2.cancel();
      }
   }

   @Inject(
      method = {"handleMoveVehicle"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;absSnapTo(DDDFF)V"
      )},
      cancellable = true
   )
   private void onVehicleMove(ClientboundMoveVehiclePacket var1, CallbackInfo var2, @Local Entity var3) {
      Vec3 var4 = var1.position();
      Class_0542 var5 = new Class_0542(var4);
      MinecraftAccess.lookups.method_36(var5);
      if (var5.method_464()) {
         var2.cancel();
      }
   }
}
