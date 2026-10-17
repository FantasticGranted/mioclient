package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.render.SkyColorModule;
import net.minecraft.client.multiplayer.ClientLevel.ClientLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ClientLevelData.class})
public class MixinProperties {
   private static SkyColorModule skycolor = Hub.field_2595.method_78(SkyColorModule.class);

   public MixinProperties() {
      super();
   }

   @Inject(
      method = {"voidDarknessOnsetRange"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onGetSkyProperties(CallbackInfoReturnable<Float> var1) {
      if (MinecraftAccess.mc.level != null) {
         if (skycolor.isToggled() && skycolor.method_125() && !MinecraftAccess.mc.level.dimension().identifier().getPath().contains("over")) {
            var1.setReturnValue(1.0F);
            var1.cancel();
         }
      }
   }
}
