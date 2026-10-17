package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Weather;
import me.mioclient.module.render.AmbienceModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.WeatherEffectRenderer;
import net.minecraft.client.renderer.state.level.WeatherRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WeatherEffectRenderer.class})
public abstract class MixinWeatherEffectRenderer implements MinecraftAccess {
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);

   public MixinWeatherEffectRenderer() {
      super();
   }

   @Inject(
      method = {"render"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderWeatherHook(Vec3 var1, WeatherRenderState var2, CallbackInfo var3) {
      if (ambience.isToggled() && ambience.worldWeather.getValue() && ambience.weather.getValue() == Weather.CLEAR) {
         var3.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"extractRenderState"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/Level;getHeight(Lnet/minecraft/world/level/levelgen/Heightmap$Types;II)I"
      )}
   )
   private int renderWeatherHook2(int var1) {
      if (!ambience.method_95()) {
         return var1;
      } else {
         byte var2 = 5;
         if (Minecraft.getInstance().options.graphicsPreset().get() != net.minecraft.client.GraphicsPreset.FAST) {
            var2 = 10;
         }

         return (int)Math.floor(mc.gameRenderer.getMainCamera().position().y) - var2 - 1;
      }
   }

   @Redirect(
      method = {"getPrecipitationAt"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/biome/Biome;getPrecipitationAt(Lnet/minecraft/core/BlockPos;I)Lnet/minecraft/world/level/biome/Biome$Precipitation;"
      )
   )
   private Biome.Precipitation renderWeatherHook3(Biome var1, BlockPos var2, int var3) {
      Biome.Precipitation var4 = var1.getPrecipitationAt(var2, var3);
      if (ambience.method_95() && var4 == Biome.Precipitation.NONE) {
         return var1.coldEnoughToSnow(var2, var3) ? Biome.Precipitation.SNOW : Biome.Precipitation.RAIN;
      } else {
         return var4;
      }
   }
}
