package me.mioclient.internal;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;

public class Class_0352 implements FormattedCharSink {
   public final float field_1151;
   public final Class_0305 field_1152;
   public final int field_1153;
   public final PoseStack field_1154;
   public float field_1155;
   public float field_1156;

   public Class_0352(PoseStack var1, int var2, float var3, Class_0305 var4) {
      super();
      this.field_1153 = var2;
      this.field_1151 = var3;
      this.field_1152 = var4;
      this.field_1154 = var1;
   }

   public int method_2(FormattedCharSequence var1, float var2, float var3) {
      if (var1 == null) {
         return 0;
      } else {
         this.field_1155 = var2 * 2F;
         this.field_1156 = var3 * 2F;
         this.method_2(this.field_1154, var1);
         return (int)(this.field_1155 / 4F);
      }
   }

   public void method_2(PoseStack var1, FormattedCharSequence var2) {
      var1.pushPose();
      var1.translate((float)FontRenderer.method_918(), (float)FontRenderer.method_916(), 0.0F);
      var1.scale(0.5F, 0.5F, 1F);
      if (!MioGlyphRenderer.field_1371.method_435()) {
         MioGlyphRenderer.field_1371.method_9(Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
      }

      var2.accept(this);
      var1.popPose();
   }

   public boolean accept(int var1, Style var2, int var3) {
      TextColor var4 = var2.getColor();
      float var5 = (float)(this.field_1153 >> 24 & 0xFF) / 255F;
      float var6;
      float var7;
      float var8;
      if (var4 != null) {
         int var9 = var4.getValue();
         var6 = (float)(var9 >> 16 & 0xFF) / 255F;
         var7 = (float)(var9 >> 8 & 0xFF) / 255F;
         var8 = (float)(var9 & 0xFF) / 255F;
      } else {
         var6 = (float)(this.field_1153 >> 16 & 0xFF) / 255F;
         var7 = (float)(this.field_1153 >> 8 & 0xFF) / 255F;
         var8 = (float)(this.field_1153 & 0xFF) / 255F;
      }

      var6 *= this.field_1151;
      var7 *= this.field_1151;
      var8 *= this.field_1151;
      this.field_1152.method_352();
      float var13 = this.field_1152
         .method_2(this.field_1154, MioGlyphRenderer.field_1371, Character.toString(var3).charAt(0), this.field_1155, this.field_1156, var6, var8, var7, var5);
      this.field_1155 += var13;
      return true;
   }
}
