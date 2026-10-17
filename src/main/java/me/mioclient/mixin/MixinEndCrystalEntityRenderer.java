package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.AttackTrackingAccess;
import me.mioclient.internal.EndCrystalRenderHook;
import me.mioclient.ducks.DuckEndCrystalRenderState;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.render.AnimationsModule;
import me.mioclient.module.render.ChamsModule;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EndCrystalRenderer;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({EndCrystalRenderer.class})
public class MixinEndCrystalEntityRenderer {
   private static AutoCrystalModule ac = Hub.field_2595.method_78(AutoCrystalModule.class);
   private static ChamsModule mod = Hub.field_2595.method_78(ChamsModule.class);
   private static AnimationsModule animations = Hub.field_2595.method_78(AnimationsModule.class);

   public MixinEndCrystalEntityRenderer() {
      super();
   }

   @Inject(
      method = {"extractRenderState(Lnet/minecraft/world/entity/boss/enderdragon/EndCrystal;Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;F)V"},
      at = {@At("HEAD")}
   )
   private void mio$stashCrystal(EndCrystal var1, EndCrystalRenderState var2, float var3, CallbackInfo var4) {
      ((DuckEndCrystalRenderState)var2).mio$setCrystal(var1);
   }

   @Inject(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void mio$renderHook(EndCrystalRenderState var1, PoseStack var2, SubmitNodeCollector var3, net.minecraft.client.renderer.state.level.CameraRenderState var4, CallbackInfo var5) {
      EndCrystal var6 = ((DuckEndCrystalRenderState)var1).mio$getCrystal();
      if (var6 != null
         && System.currentTimeMillis() - ((AttackTrackingAccess)var6).getSpawnTime() > (long)Math.max(Hub.field_2602.method_983(), 50)
         && var6.tickCount < 10
         && ac.isToggled()
         && ac.inhibit.getValue()
         && ((AttackTrackingAccess)var6).isMioAttacked()) {
         var5.cancel();
      }
   }

   @ModifyArgs(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/blaze3d/vertex/PoseStack;scale(FFF)V",
         ordinal = 0
      )
   )
   private void scaleHook(Args var1) {
      if (animations.isToggled() && animations.crystals.getValue()) {
         var1.set(0, (Float)var1.get(0) * animations.crystalScale.getValue());
         var1.set(1, (Float)var1.get(1) * animations.crystalScale.getValue());
         var1.set(2, (Float)var1.get(2) * animations.crystalScale.getValue());
      }
   }

   @WrapWithCondition(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/resources/Identifier;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"
      )}
   )
   private boolean mio$chamsSkip(
      SubmitNodeCollector var0,
      Model var1, Object var2, PoseStack var3, Identifier var4, int var5, int var6, int var7, ModelFeatureRenderer.CrumblingOverlay var8
   ) {
      EndCrystal var9 = ((DuckEndCrystalRenderState)var2).mio$getCrystal();
      return var9 == null || !mod.method_98(var9) || !mod.method_101(var9);
   }

   @ModifyExpressionValue(
      method = {"submit(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/EndCrystalRenderer;getY(F)F"
      )}
   )
   private float mio$beamFloat(float var1, @Local(argsOnly = true) EndCrystalRenderState var2) {
      return EndCrystalRenderHook.applyFloatFactor(var1, var2);
   }
}
