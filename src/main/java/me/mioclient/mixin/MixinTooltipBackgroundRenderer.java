package me.mioclient.mixin;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import me.mioclient.Hub;
import me.mioclient.module.abstract_.AbstractModule_37;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({TooltipRenderUtil.class})
public class MixinTooltipBackgroundRenderer {
   private static AbstractModule_37 tooltips = Hub.field_2595.method_78(AbstractModule_37.class);

   public MixinTooltipBackgroundRenderer() {
      super();
   }

   @Redirect(
      method = {"extractTooltipBackground"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"
      )
   )
   private static void renderHook(GuiGraphicsExtractor var0, RenderPipeline var1, Identifier var2, int var3, int var4, int var5, int var6) {
      if (tooltips.isToggled() && tooltips.method_408() != null) {
         var0.blitSprite(var1, var2, var3, var4, var5, var6, tooltips.method_408().hashCode());
         tooltips.method_16(null);
      } else {
         var0.blitSprite(var1, var2, var3, var4, var5, var6);
      }
   }
}
