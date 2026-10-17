package me.mioclient.internal;

import com.mojang.blaze3d.opengl.GlStateManager;
import me.mioclient.Hub;
import me.mioclient.enum_.ColorMode;
import me.mioclient.module.render.ShaderModule;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.opengl.GlTexture;

public class Class_0259 extends EntityOutlineShader {
   public static ShaderModule field_786 = Hub.field_2595.method_78(ShaderModule.class);
   public static final Identifier field_787 = Identifier.fromNamespaceAndPath("mio-mount", "textures/overlay.png");
   public final long field_788 = System.currentTimeMillis();

   public Class_0259() {
      super();
      this.method_251("gradient");
   }

   @Override
   public boolean method_291() {
      return field_786.isToggled() && field_786.shader.getValue() == ColorMode.GRADIENT;
   }

   @Override
   public boolean method_31(Entity var1) {
      return field_786 != null && var1 != null ? field_786.method_98(var1) : false;
   }

   @Override
   public void method_292() {
      this.field_2303.method_2("u_Texture", 0);
      int var1 = ((GlTexture)mc.getTextureManager().getTexture(field_787).getTexture()).glId();
      GlStateManager._activeTexture(33985);
      GlStateManager._bindTexture(var1);
      this.field_2303.method_2("u_Image", field_786.image.getValue());
      this.field_2303.method_2("u_Overlay", 1);
      this.field_2303.method_2("u_OverlayAlpha", (double)field_786.method_650());
      this.field_2303.method_2("u_Width", field_786.method_652() ? 0 : field_786.lineWidth.getValue());
      this.field_2303.method_2("u_FastLines", field_786.fastLines.getValue() || RenderManager.field_2003);
      this.field_2303.method_2("u_ShapeMode", 2);
      this.field_2303.method_2("u_GlowMultiplier", (double)field_786.glow.getValue().floatValue());
      this.field_2303
         .method_2(
            "u_Time", (double)((float)(System.currentTimeMillis() - this.field_788) * 0.01F * field_786.speed.getValue())
         );
      if (field_786.method_651()) {
         this.field_2303.method_2("u_FillColor", Colors.field_3321);
         this.field_2303.method_2("u_FillColor2", Colors.field_3321);
      } else {
         this.field_2303.method_2("u_FillColor", field_786.getId(field_786.fill.getValue()));
         this.field_2303.method_2("u_FillColor2", field_786.getId(field_786.fillSecond.getValue()));
      }

      this.field_2303.method_2("u_OutlineColor", field_786.outline.getValue());
      this.field_2303.method_2("u_OutlineColor2", field_786.outlineSecond.getValue());
      this.field_2303.method_2("u_Dots", field_786.type.getValue().ordinal());
      this.field_2303.method_2("u_DotsRadius", field_786.radius.getValue());
      this.field_2303.method_2("u_DotsAlpha", (double)field_786.alpha.getValue().floatValue());
      this.field_2303.method_2("u_Step", (double)field_786.step.getValue().floatValue());
   }
}
