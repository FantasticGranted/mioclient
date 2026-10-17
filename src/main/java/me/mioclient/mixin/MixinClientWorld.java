package me.mioclient.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.Random;
import me.mioclient.Hub;
import me.mioclient.api.OriginalEffectsAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Weather;
import me.mioclient.event.EntityRemoveEvent;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.render.AmbienceModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.world.level.block.Block;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity.RemovalReason;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.world.attribute.AmbientParticle;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ClientLevel.class})
public abstract class MixinClientWorld implements OriginalEffectsAccess {
   private static AmbienceModule ambience = Hub.field_2595.method_78(AmbienceModule.class);
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   @Unique
   private final Random random = new Random();

   public MixinClientWorld() {
      super();
   }

   @Shadow
   public abstract void addParticle(ParticleOptions var1, double var2, double var4, double var6, double var8, double var10, double var12);

   @ModifyArg(
      method = {"trySpawnDripParticles", "spawnParticle"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/ClientLevel;spawnFluidParticle(DDDDDLnet/minecraft/core/particles/ParticleOptions;)V"
      ),
      index = 5
   )
   private ParticleOptions randomBlockDisplayTickHook(ParticleOptions var1, @Local(argsOnly = true) BlockPos var2) {
      double var3 = MinecraftAccess.mc.gameRenderer.getMainCamera().position().distanceToSqr((double)var2.getX(), (double)var2.getY(), (double)var2.getZ());
      return ambience.isToggled() && ambience.weather.getValue() == Weather.DUSTY && ambience.worldWeather.getValue() && var3 < 25.0
         ? new AmbientParticle(this.random.nextBoolean() ? ParticleTypes.WHITE_ASH : ParticleTypes.ASH, 0.1F).particle()
         : var1;
   }

   @Inject(
      method = {"doAnimateTick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/block/Block;animateTick(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;)V",
         shift = Shift.AFTER
      )}
   )
   private void randomBlockDisplayTickHook0(
      int var1, int var2, int var3, int var4, net.minecraft.util.RandomSource var5, Block var6, MutableBlockPos var7, CallbackInfo var8
   ) {
      if (ambience.isToggled() && ambience.weather.getValue() == Weather.DUSTY && ambience.worldWeather.getValue()) {
         AmbientParticle var9 = new AmbientParticle(
            var5.nextBoolean() ? ParticleTypes.WHITE_ASH : ParticleTypes.ASH, 0.2F * ambience.amount.getValue()
         );
         if (!var9.canSpawn(var5)) {
            return;
         }

         this.addParticle(
            var9.particle(),
            (double)var7.getX() + this.random.nextDouble(),
            (double)var7.getY() + this.random.nextDouble(),
            (double)var7.getZ() + this.random.nextDouble(),
            0.0,
            0.0,
            0.0
         );
      }
   }

   @Inject(
      method = {"removeEntity"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void removeEntityHook(int var1, RemovalReason var2, CallbackInfo var3) {
      EntityRemoveEvent var4 = new EntityRemoveEvent(var1);
      MinecraftAccess.lookups.method_36(var4);
      if (var4.method_464()) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"addBreakingBlockEffect"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void addBlockBreakingParticlesHook(net.minecraft.core.BlockPos var1, net.minecraft.core.Direction var2, CallbackInfo var3) {
      if (norender.method_281()) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"addDestroyBlockEffect"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void addBlockBreakParticlesHook(net.minecraft.core.BlockPos var1, net.minecraft.world.level.block.state.BlockState var2, CallbackInfo var3) {
      if (norender.method_281()) {
         var3.cancel();
      }
   }
}
