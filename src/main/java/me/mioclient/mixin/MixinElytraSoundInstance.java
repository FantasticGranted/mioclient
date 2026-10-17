package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.movement.ElytraFlyModule;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.ElytraOnPlayerSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ElytraOnPlayerSoundInstance.class})
public abstract class MixinElytraSoundInstance extends AbstractTickableSoundInstance {
   private static ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);

   protected MixinElytraSoundInstance(SoundEvent var1, SoundSource var2, RandomSource var3) {
      super(var1, var2, var3);
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void tickHook(CallbackInfo var1) {
      if (elytrafly.isToggled() && elytrafly.muteElytra.getValue()) {
         this.volume = 0.0F;
         var1.cancel();
      }
   }
}
