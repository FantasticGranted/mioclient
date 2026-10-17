package me.mioclient.internal;

import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.font.GlyphBitmap;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import java.awt.Font;
import java.util.Locale;
import java.util.function.Function;
import me.mioclient.enum_.FontStyle;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.FormattedCharSequence;

/** Custom glyph renderer that draws font glyphs with client font styles (bold/italic/etc.). */
public class MioGlyphRenderer implements GlyphInfo {
   public static final BufferSource field_1371 = BufferSource.method_740();
   public static final BufferSource field_1372 = BufferSource.method_740();
   public static RenderType field_1373;
   public static final MultiBufferSource field_1374 = var0x -> {
      if (field_1373 != null && field_1373 != var0x) {
         Render2D.method_474();
      }

      if (!field_1372.method_435()) {
         field_1372.method_9(var0x.mode(), var0x.format());
      }

      field_1373 = var0x;
      return field_1372;
   };
   public static final int[] field_1375 = new int[32];
   public final CustomTextHandler field_1376 = new CustomTextHandler(this);
   public final Class_0305 field_1377;
   public float field_1155;
   public float field_1156;

   public MioGlyphRenderer(Class_0305 var1) {
      super();
      this.field_1377 = var1;
   }

   public static MioGlyphRenderer method_2(String var0, int var1, FontStyle var2) {
      char[] var3 = new char[1501];

      for (int var4 = 0; var4 < var3.length - 1; var4++) {
         var3[var4] = (char)var4;
      }

      var3[var3.length - 1] = 8734;

      Class_0305 var5 = switch (var2) {
         case PLAIN -> new Class_0305(new Font(var0, 0, var1), true, true);
         case BOLD -> new Class_0305(new Font(var0, 1, var1), true, true);
         case ITALIC -> new Class_0305(new Font(var0, 2, var1), true, true);
         case BOLDITALIC -> new Class_0305(new Font(var0, 3, var1), true, true);
      };
      var5.method_9(var3);
      var5.method_351();
      return new MioGlyphRenderer(var5);
   }

   public int method_2(PoseStack var1, String var2, float var3, float var4, int var5) {
      return this.method_2(var1, var2, var3, var4, var5, true);
   }

   public int method_2(PoseStack var1, String var2, double var3, double var5, int var7) {
      return this.method_2(var1, var2, (float)var3, (float)var5, var7, true);
   }

   public int method_9(PoseStack var1, String var2, float var3, float var4, int var5) {
      return this.method_2(var1, var2, var3, var4, var5, false);
   }

   public int method_9(PoseStack var1, String var2, double var3, double var5, int var7) {
      return this.method_2(var1, var2, (float)var3, (float)var5, var7, false);
   }

   public int method_5(PoseStack var1, String var2, double var3, double var5, int var7) {
      return this.method_2(var1, var2, (float)var3 - (float)this.method_473(var2) / 2F, (float)var5, var7, false);
   }

   public int method_7(PoseStack var1, String var2, double var3, double var5, int var7) {
      return this.method_2(var1, var2, (float)var3 - (float)this.method_473(var2) / 2F, (float)var5, var7, true);
   }

   public int method_2(PoseStack var1, FormattedCharSequence var2, float var3, float var4, int var5, boolean var6) {
      int var9;
      if (var6) {
         Class_0352 var8 = new Class_0352(var1, var5, 0.25F, this.field_1377);
         var9 = var8.method_2(var2, var3 + FontRenderer.method_917(), var4 + FontRenderer.method_917());
         var8 = new Class_0352(var1, var5, 1F, this.field_1377);
         var9 = Math.max(var9, var8.method_2(var2, var3, var4));
      } else {
         Class_0352 var11 = new Class_0352(var1, var5, 1F, this.field_1377);
         var9 = var11.method_2(var2, var3, var4);
      }

      return var9;
   }

   public int method_2(PoseStack var1, String var2, float var3, float var4, int var5, boolean var6) {
      int var8;
      if (var6) {
         var8 = this.method_9(var1, var2, var3 + FontRenderer.method_917(), var4 + FontRenderer.method_917(), var5, true);
         var8 = Math.max(var8, this.method_9(var1, var2, var3, var4, var5, false));
      } else {
         var8 = this.method_9(var1, var2, var3, var4, var5, false);
      }

      return var8;
   }

   public int method_9(PoseStack var1, String var2, float var3, float var4, int var5, boolean var6) {
      if (var2 == null) {
         return 0;
      } else {
         if ((var5 & -67108864) == 0) {
            var5 |= -16777216;
         }

         if (var6) {
            var5 = (var5 & 16579836) >> 2 | var5 & 0xFF000000;
         }

         this.field_1155 = var3 * 2F;
         this.field_1156 = var4 * 2F;
         this.method_2(var1, var2, var6, var5);
         return (int)(this.field_1155 / 4F);
      }
   }

   public void method_2(PoseStack var1, String var2, boolean var3, int var4) {
      float var5 = (float)(var4 >> 24 & 0xFF) / 255F;
      float var6 = (float)(var4 >> 16 & 0xFF) / 255F;
      float var7 = (float)(var4 >> 8 & 0xFF) / 255F;
      float var8 = (float)(var4 & 0xFF) / 255F;
      var1.pushPose();
      var1.translate((float)FontRenderer.method_918(), (float)FontRenderer.method_916(), 0.0F);
      var1.scale(0.5F, 0.5F, 0.5F);
      if (!field_1371.method_435()) {
         field_1371.method_9(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      }

      for (int var9 = 0; var9 < var2.length(); var9++) {
         char var10 = var2.charAt(var9);
         if (var10 == 167 && var9 + 1 < var2.length()) {
            int var13 = "0123456789abcdefklmnor".indexOf(var2.toLowerCase(Locale.ENGLISH).charAt(var9 + 1));
            if (var13 < 16) {
               if (var13 < 0) {
                  var13 = 15;
               }

               if (var3) {
                  var13 += 16;
               }

               int var12 = field_1375[var13];
               var6 = (float)(var12 >> 16 & 0xFF) / 255F;
               var7 = (float)(var12 >> 8 & 0xFF) / 255F;
               var8 = (float)(var12 & 0xFF) / 255F;
            } else {
               var6 = (float)(var4 >> 16 & 0xFF) / 255F;
               var7 = (float)(var4 >> 8 & 0xFF) / 255F;
               var8 = (float)(var4 & 0xFF) / 255F;
            }

            var9++;
         } else {
            this.field_1377.method_352();
            float var11 = this.field_1377.method_2(var1, field_1371, var10, this.field_1155, this.field_1156, var6, var8, var7, var5);
            this.method_2(var11, this.field_1377);
         }
      }

      var1.popPose();
   }

   public void method_2(float var1, Class_0305 var2) {
      this.field_1155 += var1;
   }

   public Class_0305 method_471() {
      return this.field_1377;
   }

   public int method_472() {
      return this.field_1377.method_354() / 2;
   }

   public int method_473(String var1) {
      if (var1 == null) {
         return 0;
      } else {
         int var2 = 0;
         int var4 = var1.length();
         boolean var5 = false;

         for (int var6 = 0; var6 < var4; var6++) {
            char var7 = var1.charAt(var6);
            if (var7 == 167 && var6 + 1 < var4) {
               var6++;
            } else {
               var7 = var1.charAt(var6);
               Class_0305 var3 = this.method_471();
               var2 += (int)(var3.method_5(var7) - 8F);
            }
         }

         return var2 / 2 + 1;
      }
   }

   public float method_7(char var1) {
      return (this.method_471().method_5(var1) - 8F) / 2F;
   }

   public void method_474() {
      if (field_1371.method_435()) {
         Class_0305 var1 = this.method_471();
         GlStateManager._enableBlend();
         GlStateManager._blendFuncSeparate(770, 771, 770, 771);
         var1.method_352();

         GlStateManager._disableDepthTest();
         BufferSource.method_2(field_1371);
         GlStateManager._enableDepthTest();
         var1.method_353();
      }

      method_475();
   }

   public static void method_475() {
      if (field_1372.method_435() && field_1373 != null) {
         GlStateManager._disableDepthTest();
         net.minecraft.client.renderer.rendertype.RenderTypes.debugQuads().draw(field_1372.method_742());
         GlStateManager._enableDepthTest();
         field_1373 = null;
      }
   }

   public MultiBufferSource method_476() {
      return field_1374;
   }

   public CustomTextHandler method_477() {
      return this.field_1376;
   }

   public float getAdvance() {
      return 0.0F;
   }

   public BakedGlyph bake(Function<GlyphBitmap, BakedGlyph> var1) {
      return null;
   }

   static {
      for (int var0 = 0; var0 < 32; var0++) {
         int var1 = (var0 >> 3 & 1) * 85;
         int var2 = (var0 >> 2 & 1) * 170 + var1;
         int var3 = (var0 >> 1 & 1) * 170 + var1;
         int var4 = (var0 & 1) * 170 + var1;
         if (var0 == 6) {
            var2 += 85;
         }

         if (var0 >= 16) {
            var2 /= 4;
            var3 /= 4;
            var4 /= 4;
         }

         field_1375[var0] = (var2 & 0xFF) << 16 | (var3 & 0xFF) << 8 | var4 & 0xFF;
      }
   }


   public int method_2(org.joml.Matrix3x2fStack var1, net.minecraft.util.FormattedCharSequence var2, float var3, float var4, int var5, boolean var6) {
      return method_2(me.mioclient.internal.RenderUtil.mioPose(var1), var2, var3, var4, var5, var6);
   }

   public int method_2(org.joml.Matrix3x2fStack var1, String var2, float var3, float var4, int var5, boolean var6) {
      return method_2(me.mioclient.internal.RenderUtil.mioPose(var1), var2, var3, var4, var5, var6);
   }

}
