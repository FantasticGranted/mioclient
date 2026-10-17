package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.enum_.FullbrightMode;
import me.mioclient.module.render.AmbienceModule;
import net.minecraft.client.renderer.Lightmap;
import net.minecraft.world.level.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Lightmap.class})
public class MixinLightmapTextureManager {
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);

   public MixinLightmapTextureManager() {
      super();
   }

   @Inject(
      method = {"getBrightness"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void brightnessHook(DimensionType var1, int var2, CallbackInfoReturnable<Float> var3) {
      if (ambience.isToggled() && ambience.brightness.getValue() == FullbrightMode.SCREEN) {
         var3.setReturnValue(1.0F);
      }
   }
}
