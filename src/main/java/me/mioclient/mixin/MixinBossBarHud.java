package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.render.NoRenderModule;
import me.mioclient.module.render.SkyColorModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.BossHealthOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BossHealthOverlay.class})
public class MixinBossBarHud {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static final SkyColorModule skycolor = Hub.field_2595.method_78(SkyColorModule.class);

   public MixinBossBarHud() {
      super();
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderHook(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (norender.isToggled() && norender.bossBars.getValue()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"shouldDarkenScreen"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldDarkenSkyHook(CallbackInfoReturnable<Boolean> var1) {
      if (norender.isToggled() && norender.bossBars.getValue() || skycolor.isToggled() && skycolor.method_125()) {
         var1.setReturnValue(false);
         var1.cancel();
      }
   }
}
