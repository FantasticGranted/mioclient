package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.misc.NoNarratorModule;
import net.minecraft.client.GameNarrator;
import net.minecraft.client.NarratorStatus;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({GameNarrator.class})
public class MixinNarratorManager {
   private static NoNarratorModule nonarrator = Hub.field_2595.method_78(NoNarratorModule.class);

   public MixinNarratorManager() {
      super();
   }

   @Inject(
      method = {"getStatus"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getNarrator(CallbackInfoReturnable<NarratorStatus> var1) {
      if (nonarrator.isToggled()) {
         var1.setReturnValue(NarratorStatus.OFF);
      }
   }

   @Inject(
      method = {"updateNarratorStatus"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onModeChange(CallbackInfo var1) {
      if (nonarrator.isToggled()) {
         var1.cancel();
      }
   }
}
