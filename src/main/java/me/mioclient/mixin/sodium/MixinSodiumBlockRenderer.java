package me.mioclient.mixin.sodium;

import me.mioclient.Hub;
import me.mioclient.module.render.XrayModule;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.ChunkBuildBuffers;
import net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Pseudo
@Mixin(
   targets = {"net.caffeinemc.mods.sodium.client.render.chunk.compile.pipeline.BlockRenderer"},
   remap = false
)
public class MixinSodiumBlockRenderer {
   private static XrayModule xray = Hub.field_2595.method_78(XrayModule.class);

   public MixinSodiumBlockRenderer() {
      super();
   }

   @Inject(
      method = {"renderModel"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderModel(BlockRenderContext var1, ChunkBuildBuffers var2, CallbackInfo var3) {
      if (Minecraft.getInstance().player != null) {
         if (xray.isToggled()) {
            try {
               BlockPos pos = (BlockPos)(Object)BlockRenderContext.class.getMethod("pos").invoke(var1);
               BlockState state = (BlockState)(Object)BlockRenderContext.class.getMethod("state").invoke(var1);
               if (!xray.method_2(pos, state.getBlock())) {
                  var3.cancel();
               }
            } catch (Exception e) {
            }
         }
      }
   }
}
