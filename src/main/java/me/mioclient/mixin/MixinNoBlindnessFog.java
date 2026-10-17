package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.fog.FogData;
import net.minecraft.client.renderer.fog.environment.BlindnessFogEnvironment;
import net.minecraft.client.renderer.fog.environment.DarknessFogEnvironment;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlindnessFogEnvironment.class, DarknessFogEnvironment.class})
public abstract class MixinNoBlindnessFog implements MinecraftAccess {
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinNoBlindnessFog() {
      super();
   }

   @Inject(
      method = {"setupFog"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void blindnessHook(FogData var1, Camera var2, ClientLevel var3, float var4, DeltaTracker var5, CallbackInfo var6) {
      if (norender.isToggled() && norender.blindness.getValue()) {
         var6.cancel();
      }
   }

   @Inject(
      method = {"getModifiedDarkness"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void blindnessHook2(LivingEntity var1, float var2, float var3, CallbackInfoReturnable<Float> var4) {
      if (norender.isToggled() && norender.blindness.getValue()) {
         var4.setReturnValue(var2);
      }
   }
}
