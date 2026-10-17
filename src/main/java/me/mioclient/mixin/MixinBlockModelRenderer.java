package me.mioclient.mixin;

import me.mioclient.module.render.XrayModule;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ModelBlockRenderer.class})
public class MixinBlockModelRenderer {
   public MixinBlockModelRenderer() {
      super();
   }

   @Inject(
      method = {"tesselateBlock"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderSmoothHook(
      BlockQuadOutput var1,
      float var2,
      float var3,
      float var4,
      BlockAndTintGetter var5,
      BlockPos var6,
      BlockState var7,
      BlockStateModel var8,
      long var9,
      CallbackInfo var12
   ) {
      if (XrayModule.method_844().isToggled() && !XrayModule.method_844().method_2(var6, var7.getBlock())) {
         var12.cancel();
      }
   }
}
