package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.render.AmbienceModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biome.Precipitation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Biome.class})
public class MixinBiome {
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);

   public MixinBiome() {
      super();
   }

   @Inject(
      method = {"getPrecipitationAt"},
      at = {@At("HEAD")},
      cancellable = true
   )
    private void getPrecipitationHook(BlockPos var1, int var2, CallbackInfoReturnable<Precipitation> var3) {
       if (ambience.isToggled() && ambience.worldWeather.getValue()) {
          var3.cancel();
          var3.setReturnValue(ambience.weather.getValue().getPrecipitation());
       }
    }
}
