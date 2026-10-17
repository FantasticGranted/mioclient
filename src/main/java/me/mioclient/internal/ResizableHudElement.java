package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.module.abstract_.AbstractModule_26;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import com.mojang.blaze3d.opengl.GlStateManager;

/** HUD element with a Size setting and a corner resize handle for scaling the module in the editor. */
public class ResizableHudElement extends HudElement {
   public static final Identifier field_430 = Identifier.fromNamespaceAndPath("mio", "textures/triangle.png");
   public static ResizableHudElement field_431;
   public final Setting<Float> size = new CustomSetting3<>(
      "Size", 1F, 0.5F, 2F
   );
   public final TimedAnimation field_433 = new TimedAnimation();
   public boolean field_434;

   public ResizableHudElement(AbstractModule_26 var1) {
      super(var1);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      super.method_2(var1, var2, var3, var5);
      float[] var7 = this.field_461.method_31();
      var2.pushPose();
      if (this.field_434) {
         var2.scale(this.method_174(), this.method_174(), 1F);
         var2.translate(this.method_59() / this.method_174(), this.method_60() / this.method_174(), 0.0F);
      } else {
         var2.translate(this.method_59(), this.method_60(), 0.0F);
      }

      this.field_461.method_2(var1);
      if (!field_460 && this.field_434) {
         boolean var8 = this.method_39(var3, var5);
         if (field_431 == this) {
            double var9 = (double)var7[0];
            double var11 = (double)var7[1];
            double var13 = Math.min((var3 - (double)this.method_59()) / var9, (var5 - (double)this.method_60()) / var11);
            this.size.method_78((float)MathUtil.method_2(var13, 2));
         }

         if (var8 && this.method_175(1)) {
            this.size.method_78(1F);
         }

         if (var8 && this.method_175(0)) {
            field_431 = this;
         } else if (!this.method_175(0)) {
            field_431 = null;
         }

         if (field_431 == this) {
            this.field_433.method_2(0.9F, 250L);
         } else if (var8) {
            this.field_433.method_2(0.4F, 250L);
         } else {
            this.field_433.method_2(0.2F, 250L);
         }

         var2.pushPose();
         float var15 = 0.01953125F;
         var2.scale(var15, var15, 5e+02F);
         var2.translate((var7[0] - 5F) / var15, (var7[1] - 5F) / var15, 0.0F);
         Color var10 = Colors.method_959();
         GlStateManager._enableBlend();

         var1.blit(net.minecraft.client.renderer.RenderPipelines.GUI_TEXTURED, field_430, 0, 0, 0, 0, 256, 256, 256, 256);

         GlStateManager._disableBlend();
         var2.popPose();
      }

      var2.popPose();
   }

   @Override
   public float[] method_31() {
      float[] var1 = this.field_461.method_31();
      var1[0] *= this.method_174();
      var1[1] *= this.method_174();
      return var1;
   }

   public void method_2(AbstractModule_26 var1) {
      this.field_434 = true;
      var1.register(this.size);
   }

   public float method_174() {
      return this.size.getValue();
   }

   public boolean method_39(double var1, double var3) {
      if (!this.field_434) {
         return false;
      } else {
         float[] var5 = this.method_193();
         float var6 = this.method_59() + Math.max(var5[0], 4F);
         float var7 = this.method_60() + Math.max(var5[1], 4F);
         return var1 >= (double)(var6 - 5F)
            && var1 <= (double)var6
            && var3 >= (double)(var7 - 5F)
            && var3 <= (double)var7;
      }
   }

   public boolean method_175(int var1) {
      return GLFW.glfwGetMouseButton(mc.getWindow().handle(), var1) == 1;
   }
}
