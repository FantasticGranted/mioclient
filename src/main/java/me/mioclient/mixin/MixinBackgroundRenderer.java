package me.mioclient.mixin;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.module.render.NoRenderModule;
import me.mioclient.module.render.SkyColorModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.FogRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({FogRenderer.class})
public class MixinBackgroundRenderer {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static SkyColorModule skycolor = Hub.field_2595.method_78(SkyColorModule.class);

   public MixinBackgroundRenderer() {
      super();
   }

   @Inject(
      method = {"setupFog"},
      at = {@At("TAIL")}
   )
   private void onApplyFog(Camera var0, int var1, DeltaTracker var2, float var3, ClientLevel var4, CallbackInfoReturnable<FogData> var5) {
      FogData var6 = var5.getReturnValue();
      if (var6 == null) {
         return;
      }

      if (norender.isToggled() && norender.fog.getValue()) {
         float var7 = (float)(Minecraft.getInstance().options.getEffectiveRenderDistance() * 16) * norender.range.getValue();
         var6.environmentalStart = 0.0F;
         var6.environmentalEnd = var7;
         var6.renderDistanceStart = 0.0F;
         var6.renderDistanceEnd = var7;
      }

      if (skycolor.isToggled() && skycolor.method_125()) {
         Color var8 = skycolor.fog.getValue();
         var6.color.set((float)var8.getRed() / 255.0F, (float)var8.getGreen() / 255.0F, (float)var8.getBlue() / 255.0F, (float)var8.getAlpha() / 255.0F);
      }
   }
}
