package me.mioclient.mixin;

import me.mioclient.internal.RenderManager;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({BlockEntityRenderer.class})
public interface MixinBlockEntityRenderer {
   @Inject(
      method = {"getViewDistance"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getRenderDistanceHook(CallbackInfoReturnable<Integer> var1) {
      if (RenderManager.field_4422) {
         var1.setReturnValue(1024);
      }
   }
}
