package me.mioclient.mixin;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.SoundPlayEvent;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({SoundEngine.class})
public class MixinSoundSystem {
   public MixinSoundSystem() {
      super();
   }

   @Inject(
      method = {"play(Lnet/minecraft/client/resources/sounds/SoundInstance;)Lnet/minecraft/client/sounds/SoundEngine$PlayResult;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void playHook(SoundInstance var1, CallbackInfoReturnable<SoundEngine.PlayResult> var2) {
      SoundPlayEvent var3 = new SoundPlayEvent(var1);
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.setReturnValue(SoundEngine.PlayResult.NOT_STARTED);
      }
   }
}
