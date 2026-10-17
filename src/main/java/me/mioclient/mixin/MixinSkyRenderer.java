package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.render.SkyColorModule;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.SkyRenderer;
import net.minecraft.client.renderer.state.level.SkyRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SkyRenderer.class})
public class MixinSkyRenderer {
   private static SkyColorModule skycolor = Hub.field_2595.method_78(SkyColorModule.class);

   public MixinSkyRenderer() {
      super();
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At(
         value = "FIELD",
         target = "Lnet/minecraft/client/renderer/state/level/SkyRenderState;skyColor:I",
         opcode = 181,
         shift = Shift.AFTER
      )}
   )
   private void getSkyColorHook(ClientLevel var1, float var2, Camera var3, SkyRenderState var4, CallbackInfo var5) {
      if (skycolor.isToggled() && skycolor.method_125()) {
         var4.skyColor = skycolor.sky.getValue().getRGB();
      }
   }
}
