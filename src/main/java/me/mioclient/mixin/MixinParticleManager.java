package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.ParticleEvent;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ParticleEngine.class})
public abstract class MixinParticleManager {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinParticleManager() {
      super();
   }

   @Shadow
   @Nullable
   private <T extends ParticleOptions> Particle makeParticle(T var1, double var2, double var4, double var6, double var8, double var10, double var12) {
      return null;
   }

   @Inject(
      method = {"add(Lnet/minecraft/client/particle/Particle;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void addParticle(Particle var1, CallbackInfo var2) {
      ParticleEvent var3 = new ParticleEvent(var1);
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"makeParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)Lnet/minecraft/client/particle/Particle;"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void addParticleHook(
      ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12, CallbackInfoReturnable<Particle> var14
   ) {
      if (norender.method_2(var1)) {
         var14.setReturnValue(this.makeParticle(var1, var2, var4, var6, var8, var10, var12));
         var14.cancel();
      }
   }
}
