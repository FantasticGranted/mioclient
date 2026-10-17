package me.mioclient.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.internal.Colors;
import me.mioclient.internal.MioRenderLayers;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({EquipmentLayerRenderer.class})
public class MixinEquipmentLayerRenderer {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);

   public MixinEquipmentLayerRenderer() {
      super();
   }

   @Redirect(
      method = {"renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/rendertype/RenderTypes;armorCutoutNoCull(Lnet/minecraft/resources/Identifier;)Lnet/minecraft/client/renderer/rendertype/RenderType;",
         ordinal = 0
      )
   )
   private RenderType mio$armorLayer(Identifier var1) {
      float var2 = norender.method_283() * norender.method_284();
      return var2 == 1.0F ? RenderTypes.armorCutoutNoCull(var1) : MioRenderLayers.method_2(var1);
   }

   @ModifyArgs(
      method = {"renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V",
         ordinal = 0
      )
   )
   private void mio$armorColor(Args var1) {
      float var2 = norender.method_283() * norender.method_284();
      if (var2 < 1.0F) {
         Color var3 = new Color((Integer)var1.get(6));
         var1.set(6, Colors.method_9(var3, var2));
      }
   }
}
