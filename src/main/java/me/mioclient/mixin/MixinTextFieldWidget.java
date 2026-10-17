package me.mioclient.mixin;

import java.awt.Color;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.ChestSearchBar;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import com.mojang.blaze3d.vertex.PoseStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({EditBox.class})
public class MixinTextFieldWidget {
   @Shadow
   private boolean bordered;
   private PoseStack lastmatrix;

   public MixinTextFieldWidget() {
      super();
   }

   @Inject(
      method = {"extractWidgetRenderState"},
      at = {@At("HEAD")}
   )
   public void renderButton(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      this.lastmatrix = RenderUtil.mioPose(var1.pose());
   }

   @Redirect(
      method = {"extractWidgetRenderState"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/EditBox;isBordered()Z",
         ordinal = 0
      )
   )
   public boolean drawsBackground(EditBox var1) {
      if (var1 == ChestSearchBar.field_3026) {
         int var2 = var1.getX() - 1;
         int var3 = var1.getY() - 1;
         int var4 = var1.getWidth() + 1;
         int var5 = var1.getHeight() + 1;
         RenderUtil.field_2672
            .method_2(this.lastmatrix, (float)var2, (float)var3, (float)(var2 + var4), (float)(var3 + var5), new Color(135, 135, 135, 255).getRGB());
         RenderUtil.field_2672
            .method_2(this.lastmatrix, (float)var2, (float)var3, (float)(var2 + var4 - 1), (float)(var3 + 1), new Color(85, 85, 85, 255).getRGB());
         RenderUtil.field_2672
            .method_2(this.lastmatrix, (float)var2, (float)var3, (float)(var2 + 1), (float)(var3 + var5 - 1), new Color(85, 85, 85, 255).getRGB());
         RenderUtil.field_2672.method_2(this.lastmatrix, (float)(var2 + 1), (float)(var3 + var5 - 1), (float)(var2 + var4 - 1), (float)(var3 + var5), -1);
         RenderUtil.field_2672.method_2(this.lastmatrix, (float)(var2 + var4 - 1), (float)(var3 + 1), (float)(var2 + var4), (float)(var3 + var5), -1);
         return false;
      } else {
         return this.bordered;
      }
   }
}
