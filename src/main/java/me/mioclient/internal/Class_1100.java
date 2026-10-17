package me.mioclient.internal;

import com.mojang.blaze3d.opengl.GlStateManager;
import me.mioclient.Hub;
import me.mioclient.enum_.ColorMode;
import me.mioclient.module.render.ShaderModule;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import com.mojang.blaze3d.opengl.GlTexture;

public class Class_1100 extends EntityOutlineShader {
   public static final Identifier field_3411 = Identifier.fromNamespaceAndPath("mio-mount", "textures/overlay.png");
   public static ShaderModule field_786 = Hub.field_2595.method_78(ShaderModule.class);
   public final long field_3412 = System.currentTimeMillis();

   public Class_1100() {
      super();
      this.method_251("rainbow");
   }

   @Override
   public boolean method_291() {
      return field_786.isToggled() && field_786.shader.getValue() == ColorMode.RAINBOW;
   }

   @Override
   public boolean method_31(Entity var1) {
      return field_786 != null && var1 != null ? field_786.method_98(var1) : false;
   }

   @Override
   public void method_292() {
      this.field_2303.method_2("u_Texture", 0);
      int var1 = ((GlTexture)mc.getTextureManager().getTexture(field_3411).getTexture()).glId();
      GlStateManager._activeTexture(33985);
      GlStateManager._bindTexture(var1);
      this.field_2303.method_2("u_Image", field_786.image.getValue());
      this.field_2303.method_2("u_Overlay", 1);
      this.field_2303.method_2("u_OverlayAlpha", (double)field_786.method_650());
      this.field_2303.method_2("u_Fill", field_786.method_651() ? Colors.field_3321 : field_786.getId(field_786.fill.getValue()));
      this.field_2303
         .method_2(
            "u_Fill_Offset",
            (double)((float)(System.currentTimeMillis() - this.field_3412) * 0.001F * field_786.fillSpeed.getValue())
         );
      this.field_2303.method_2("u_Fill_Strength", (double)field_786.fillStrength.getValue().floatValue());
      this.field_2303.method_2("u_Outline", field_786.outline.getValue());
      this.field_2303
         .method_2(
            "u_Outline_Offset",
            (double)((float)(System.currentTimeMillis() - this.field_3412) * 0.001F * field_786.outlineSpeed.getValue())
         );
      this.field_2303.method_2("u_Outline_Strength", (double)field_786.outlineStrength.getValue().floatValue());
      this.field_2303.method_2("u_Radius", field_786.method_652() ? 0 : field_786.lineWidth.getValue());
      this.field_2303.method_2("u_GlowMultiplier", (double)field_786.glow.getValue().floatValue());
      this.field_2303.method_2("u_FastLines", field_786.fastLines.getValue() || RenderManager.field_2003);
      this.field_2303.method_2("u_Dots", field_786.type.getValue().ordinal());
      this.field_2303.method_2("u_DotsRadius", field_786.radius.getValue());
      this.field_2303.method_2("u_DotsAlpha", (double)field_786.alpha.getValue().floatValue());
   }
}
