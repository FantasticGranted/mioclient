package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.util.Collections;
import java.util.List;
import me.mioclient.module.abstract_.AbstractModule_26;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

/** HUD element that renders a list of {@link HudTextElement} lines (e.g. multi-row info displays). */
public class TextHudElement extends HudElement {
   public final List<? extends HudTextElement> field_2298;

   public TextHudElement(AbstractModule_26 var1, HudTextElement var2) {
      this(var1, Collections.singletonList(var2));
   }

   public TextHudElement(AbstractModule_26 var1, List<? extends HudTextElement> var2) {
      super(var1);
      this.field_2298 = var2;
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      super.method_2(var1, var2, var3, var5);
      float var7 = this.method_194((float)this.method_66());

      for (HudTextElement var9 : this.field_2298) {
         var2.pushPose();
         float var10 = var9.method_143();
         var10 = (float)Mth.clamp(
            1.0
               - Math.pow((double)(1F - var10), 4.0),
            0.0,
            1.0
         );
         Color var11 = this.method_2(var7, var9);
         float var12 = var9.field_360.method_45();
         float var13 = var12 * (1F - var10);
         var2.translate(this.method_176(var12) - var13 * (float)this.method_195(), var7, 0.0F);
         var9.method_2(var1, 0.0F, 0.0F, var11);
         if (var10 != 0.0F) {
            var7 += ((float)this.method_66() * var10 + 1F) * (float)this.method_196();
         }

         var2.popPose();
      }
   }

   @Override
   public float[] method_31() {
      float var1 = 0.0F;
      float var2 = 0.0F;

      for (HudTextElement var4 : this.field_2298) {
         var4.method_142();
         if (var4.method_143() != 0.0F) {
            float var5 = var4.method_143();
            var5 = (float)Mth.clamp(
               1.0
                  - Math.pow((double)(1F - var5), 4.0),
               0.0,
               1.0
            );
            float var6 = FontRenderer.field_3143.method_221(var4.method_15().getString());
            var1 += (float)this.method_66() * var5 + 1F;
            if (var6 > var2) {
               var2 = var6;
            }
         }
      }

      if (!field_460 && var2 == 0.0F) {
         var2 = 5e+01F;
         var1 = (float)this.method_66();
      }

      return new float[]{var2, var1};
   }

   public List<? extends HudTextElement> method_694() {
      return this.field_2298;
   }

   public Color method_2(float var1, HudTextElement var2) {
      return this.field_461.method_9(var1);
   }
}
