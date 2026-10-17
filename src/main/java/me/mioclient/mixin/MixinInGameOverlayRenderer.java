package me.mioclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ScreenEffectRenderer.class})
public class MixinInGameOverlayRenderer {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinInGameOverlayRenderer() {
      super();
   }

    @Inject(
       method = {"renderFire"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private static void onRenderFireOverlay(PoseStack var0, MultiBufferSource var1, TextureAtlasSprite var2, CallbackInfo var3) {
       if (norender.isToggled() && norender.fire.getValue()) {
          var3.cancel();
       }
    }

    @Inject(
       method = {"renderWater"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private static void onRenderUnderwaterOverlay(Minecraft var0, PoseStack var1, MultiBufferSource var2, CallbackInfo var3) {
       if (norender.isToggled() && norender.blindness.getValue()) {
          var3.cancel();
       }
    }

    @Inject(
       method = {"renderTex"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private static void render(TextureAtlasSprite var0, PoseStack var1, MultiBufferSource var2, CallbackInfo var3) {
       if (norender.isToggled() && norender.blindness.getValue()) {
          var3.cancel();
       }
    }
}
