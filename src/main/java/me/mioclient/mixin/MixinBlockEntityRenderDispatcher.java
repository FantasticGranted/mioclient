package me.mioclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.module.render.ESPModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({BlockEntityRenderDispatcher.class})
public class MixinBlockEntityRenderDispatcher {
   private static final ESPModule esp = Hub.field_2595.method_78(ESPModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinBlockEntityRenderDispatcher() {
      super();
   }

   @Inject(
      method = {"submit(Lnet/minecraft/client/renderer/blockentity/state/BlockEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderHook(BlockEntityRenderState var1, PoseStack var2, SubmitNodeCollector var3, CameraRenderState var4, CallbackInfo var5) {
      if (!norender.ignoreESP.getValue() || !esp.method_1065()) {
         if (norender.isToggled()
            && norender.tileEntities.getValue()
            && var1.blockPos.distSqr(Minecraft.getInstance().gameRenderer.getMainCamera().blockPosition())
               > (double)(norender.tileDistance.getValue() * norender.tileDistance.getValue())) {
            var5.cancel();
         }
      }
   }
}
