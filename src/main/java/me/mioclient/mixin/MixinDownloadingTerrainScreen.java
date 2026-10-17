package me.mioclient.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO26.1: ReceivingLevelScreen no longer exists in 26.1 (ProgressScreen replaced it); soft-mixin so it is skipped when absent.
@Pseudo
@Mixin(targets = "net.minecraft.client.gui.screens.ReceivingLevelScreen")
public class MixinDownloadingTerrainScreen {
   public MixinDownloadingTerrainScreen() {
      super();
   }

   @Inject(
      method = {"shouldCloseOnEsc"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldCloseOnEscHook(CallbackInfoReturnable<Boolean> var1) {
      var1.setReturnValue(true);
   }
}
