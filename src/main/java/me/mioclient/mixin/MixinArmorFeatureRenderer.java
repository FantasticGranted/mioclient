package me.mioclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({HumanoidArmorLayer.class})
public class MixinArmorFeatureRenderer {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinArmorFeatureRenderer() {
      super();
   }

   @Inject(
      method = {"submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/EntityRenderState;FF)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void mio$hideArmor(PoseStack var1, SubmitNodeCollector var2, int var3, EntityRenderState var4, float var5, float var6, CallbackInfo var7) {
      if (norender.method_283() == 0.0F) {
         var7.cancel();
      }
   }
}
