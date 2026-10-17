package me.mioclient.internal;

import com.mojang.blaze3d.opengl.GlStateManager;
import me.mioclient.Hub;
import me.mioclient.enum_.ColorMode;
import me.mioclient.module.render.ShaderModule;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.opengl.GlTexture;

public class Class_0534 extends EntityOutlineShader {
   public static final Identifier field_1682 = Identifier.fromNamespaceAndPath("mio-mount", "textures/overlay.png");
   public static ShaderModule field_786 = Hub.field_2595.method_78(ShaderModule.class);

   public Class_0534() {
      super();
      this.method_251("bloom");
   }

   @Override
   public boolean method_291() {
      return field_786.isToggled() && field_786.shader.getValue() == ColorMode.BLOOM;
   }

   @Override
   public boolean method_31(Entity var1) {
      return field_786 != null && var1 != null ? field_786.method_98(var1) : false;
   }

   @Override
   public void method_292() {
      this.field_2303.method_2("u_Texture", 0);
      int var1 = ((GlTexture)mc.getTextureManager().getTexture(field_1682).getTexture()).glId();
      GlStateManager._activeTexture(33985);
      GlStateManager._bindTexture(var1);
      this.field_2303.method_2("u_Image", field_786.image.getValue());
      this.field_2303.method_2("u_Overlay", 1);
      this.field_2303.method_2("u_OverlayAlpha", (double)field_786.method_650());
      this.field_2303.method_2("u_Width", field_786.method_652() ? 0 : field_786.lineWidth.getValue());
      this.field_2303.method_2("u_FastLines", field_786.fastLines.getValue() || RenderManager.field_2003);
      this.field_2303.method_2("u_ShapeMode", 2);
      this.field_2303.method_2("u_GlowMultiplier", (double)field_786.glow.getValue().floatValue());
      this.field_2303.method_2("u_GlowQuality", field_786.quality.getValue());
      this.field_2303
         .method_2(
            "u_FillColor",
            field_786.method_651() ? Colors.method_29(field_786.outline.getValue()) : field_786.getId(field_786.fill.getValue())
         );
      this.field_2303.method_2("u_OutlineColor", field_786.outline.getValue());
      this.field_2303.method_2("u_Dots", field_786.type.getValue().ordinal());
      this.field_2303.method_2("u_DotsRadius", field_786.radius.getValue());
      this.field_2303.method_2("u_DotsAlpha", (double)field_786.alpha.getValue().floatValue());
   }
}
