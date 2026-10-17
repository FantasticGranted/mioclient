package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import me.mioclient.Hub;
import me.mioclient.api.AttackTrackingAccess;
import me.mioclient.internal.EndCrystalRenderHook;
import me.mioclient.ducks.DuckEndCrystalRenderState;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.render.AnimationsModule;
import net.minecraft.client.model.object.crystal.EndCrystalModel;
import net.minecraft.client.renderer.entity.state.EndCrystalRenderState;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EndCrystalModel.class})
public class MixinEndCrystalModel {
   private static AutoCrystalModule mio$ac = Hub.field_2595.method_78(AutoCrystalModule.class);
   private static AnimationsModule mio$animations = Hub.field_2595.method_78(AnimationsModule.class);

   @ModifyVariable(
      method = {"setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V"},
      at = @At(
         value = "STORE",
         ordinal = 0
      )
   )
   private float mio$rotationSpeed(float var1, @Local(argsOnly = true) EndCrystalRenderState var2) {
      EndCrystal var3 = ((DuckEndCrystalRenderState)var2).mio$getCrystal();
      float var4 = var1;
      if (mio$animations.isToggled() && mio$animations.crystals.getValue()) {
         var4 *= mio$animations.rotationSpeed.getValue();
      }

      if (var3 != null
         && var3.tickCount < 10
         && mio$ac.isToggled()
         && mio$ac.inhibit.getValue()
         && ((AttackTrackingAccess)var3).isMioAttacked()) {
         var4 *= 1.5F;
      }

      return var4;
   }

   @ModifyExpressionValue(
      method = {"setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/entity/EndCrystalRenderer;getY(F)F"
      )}
   )
   private float mio$floatFactor(float var1, @Local(argsOnly = true) EndCrystalRenderState var2) {
      return EndCrystalRenderHook.applyFloatFactor(var1, var2);
   }

   @Inject(
      method = {"setupAnim(Lnet/minecraft/client/renderer/entity/state/EndCrystalRenderState;)V"},
      at = {@At("TAIL")}
   )
   private void mio$parts(EndCrystalRenderState var1, CallbackInfo var2) {
      EndCrystalModel var3 = (EndCrystalModel)(Object)this;
      var3.base.visible = var3.base.visible && !mio$animations.method_136();
      var3.outerGlass.visible = var3.outerGlass.visible && !mio$animations.method_134();
      var3.innerGlass.visible = var3.innerGlass.visible && !mio$animations.method_133();
      var3.cube.visible = var3.cube.visible && !mio$animations.method_135();
   }
}
