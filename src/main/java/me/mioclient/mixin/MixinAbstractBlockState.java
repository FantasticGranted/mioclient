package me.mioclient.mixin;

import me.mioclient.module.render.XrayModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour.BlockStateBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockStateBase.class})
public class MixinAbstractBlockState {
   public MixinAbstractBlockState() {
      super();
   }

   @Inject(
      method = {"getShadeBrightness"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getAmbientOcclusionLightLevelHook(BlockGetter var1, BlockPos var2, CallbackInfoReturnable<Float> var3) {
      if (XrayModule.method_844().isToggled()) {
         var3.cancel();
         var3.setReturnValue(1.0F);
      }
   }
}
