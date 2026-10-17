package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.toasts.SystemToast.SystemToastId;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.TutorialToast;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(
   targets = {"net/minecraft/client/gui/components/toasts/ToastManager$ToastInstance"}
)
public class MixinToastManagerEntry {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   @Final
   @Shadow
   private Toast toast;

   public MixinToastManagerEntry() {
      super();
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void draw(GuiGraphicsExtractor var1, int var2, CallbackInfo var3) {
      if (norender.isToggled()
         && (
            this.toast.getToken() == SystemToastId.UNSECURE_SERVER_WARNING && norender.unsecureServer.getValue()
               || this.toast instanceof TutorialToast && norender.tutorialToast.getValue()
         )) {
         var3.cancel();
      }
   }
}
