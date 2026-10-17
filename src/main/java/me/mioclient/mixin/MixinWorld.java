package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mioclient.Hub;
import me.mioclient.enum_.Weather;
import me.mioclient.module.render.AmbienceModule;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Level.class})
public class MixinWorld {
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);

   public MixinWorld() {
      super();
   }

   @Inject(
      method = {"getRainLevel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getRainGradientHook(CallbackInfoReturnable<Float> var1) {
      if (ambience.worldWeather.getValue() && ambience.isToggled()) {
         var1.setReturnValue(ambience.weather.getValue() == Weather.CLEAR ? 0.0F : 1.0F);
         var1.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"<init>"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/RandomSource;create()Lnet/minecraft/util/RandomSource;"
      )},
      require = 0
   )
   private RandomSource initHook(RandomSource var1) {
      return RandomSource.createThreadSafe();
   }
}
