package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.internal.DisconnectConfirmScreen;
import me.mioclient.module.exploit.IllegalDisconnectModule;
import me.mioclient.module.misc.AntiQuitModule;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({PauseScreen.class})
public abstract class MixinGameMenuScreen extends Screen {
   private static IllegalDisconnectModule illegaldisconnect = Hub.field_2595.method_78(IllegalDisconnectModule.class);
   @Unique
   private boolean mio$ignoreDisconnect;
   private static AntiQuitModule antiquit = Hub.field_2595.method_78(AntiQuitModule.class);

   @Shadow
   private void lambda$createPauseMenu$7() {
   }

   protected MixinGameMenuScreen(Component var1) {
      super(var1);
   }

   @Inject(
      method = {"lambda$createPauseMenu$7"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void disconnectHook(CallbackInfo var1) {
      if (antiquit.isToggled() && antiquit.disconnect.getValue() && !this.mio$ignoreDisconnect) {
         this.minecraft.setScreen(new DisconnectConfirmScreen(() -> {
            this.mio$ignoreDisconnect = true;
            this.lambda$createPauseMenu$7();
         }));
         var1.cancel();
      } else {
         if (illegaldisconnect.isToggled() && this.minecraft.player != null && !this.minecraft.isLocalServer()) {
            illegaldisconnect.method_202();
            var1.cancel();
         }
      }
   }
}
