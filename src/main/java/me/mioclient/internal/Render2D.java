package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.util.FormattedCharSequence;
import org.joml.Matrix4f;
import com.mojang.blaze3d.opengl.GlStateManager;

/**
 * 2D GUI drawing primitives batched through a shared buffer: filled rects, outlines,
 * horizontal/vertical gradients and custom-font text.
 */
public class Render2D {
   public static final BufferSource BUFFER = BufferSource.method_740();

   public Render2D() {
      super();
   }

   public static void method_5(PoseStack var0, float var1, float var2, float var3, float var4, Color var5) {
      method_4(var0, var1, var3, var2 + var4, var3 + var4, var5);
   }

   public static void method_7(PoseStack var0, float var1, float var2, float var3, float var4, Color var5) {
      method_4(var0, var1, var2 + var4, var1 + var4, var3, var5);
   }

   public static void method_29(PoseStack var0, float var1, float var2, float var3, float var4, Color var5) {
      method_5(var0, var1, var3, var2, 1.0F, var5);
      method_7(var0, var3, var2, var4, 1.0F, var5);
      method_5(var0, var1, var3, var4, 1.0F, var5);
      method_7(var0, var1, var2, var4, 1.0F, var5);
   }

   public static void method_2(PoseStack var0, float var1, float var2, float var3, float var4, float var5, Color var6) {
      method_5(var0, var1, var3, var2, var5, var6);
      method_7(var0, var3, var2, var4, var5, var6);
      method_5(var0, var1, var3, var4, var5, var6);
      method_7(var0, var1, var2, var4, var5, var6);
   }

   public static void method_4(PoseStack var0, float var1, float var2, float var3, float var4, Color var5) {
      method_9(var0, var1, var2, var3, var4, var5.hashCode());
   }

   public static void method_9(PoseStack var0, float var1, float var2, float var3, float var4, int var5) {
      float var6 = (float)(var5 >> 24 & 0xFF) / 255.0F;
      float var7 = (float)(var5 >> 16 & 0xFF) / 255.0F;
      float var8 = (float)(var5 >> 8 & 0xFF) / 255.0F;
      float var9 = (float)(var5 & 0xFF) / 255.0F;
      if (var6 != 0.0F) {
         BUFFER.addVertex(var0.last().pose(), var1, var4, 0.0F).setColor(var7, var8, var9, var6);
         BUFFER.addVertex(var0.last().pose(), var3, var4, 0.0F).setColor(var7, var8, var9, var6);
         BUFFER.addVertex(var0.last().pose(), var3, var2, 0.0F).setColor(var7, var8, var9, var6);
         BUFFER.addVertex(var0.last().pose(), var1, var2, 0.0F).setColor(var7, var8, var9, var6);
      }
   }

   public static void method_9(Matrix4f var0, float var1, float var2, float var3, float var4, int var5, int var6, int var7, int var8) {
      float[] var9 = new float[]{(float)(var5 >> 16 & 0xFF) / 255.0F, (float)(var5 >> 8 & 0xFF) / 255.0F, (float)(var5 & 0xFF) / 255.0F};
      float[] var10 = new float[]{(float)(var6 >> 16 & 0xFF) / 255.0F, (float)(var6 >> 8 & 0xFF) / 255.0F, (float)(var6 & 0xFF) / 255.0F};
      float[] var11 = new float[]{(float)(var7 >> 16 & 0xFF) / 255.0F, (float)(var7 >> 8 & 0xFF) / 255.0F, (float)(var7 & 0xFF) / 255.0F};
      float[] var12 = new float[]{(float)(var8 >> 16 & 0xFF) / 255.0F, (float)(var8 >> 8 & 0xFF) / 255.0F, (float)(var8 & 0xFF) / 255.0F};
      BUFFER.addVertex(var0, var1, var2 + var4, 0.0F).setColor(var12[0], var12[1], var12[2], 1.0F);
      BUFFER.addVertex(var0, var1 + var3, var2 + var4, 0.0F).setColor(var11[0], var11[1], var11[2], 1.0F);
      BUFFER.addVertex(var0, var1 + var3, var2, 0.0F).setColor(var10[0], var10[1], var10[2], 1.0F);
      BUFFER.addVertex(var0, var1, var2, 0.0F).setColor(var9[0], var9[1], var9[2], 1.0F);
   }

   public static void method_9(Matrix4f var0, float var1, float var2, float var3, float var4, int... var5) {
      if (var5.length >= 2) {
         float[][] var6 = new float[var5.length][4];

         for (int var7 = 0; var7 < var5.length; var7++) {
            var6[var7] = new float[]{
               (float)(var5[var7] >> 24 & 0xFF) / 255.0F,
               (float)(var5[var7] >> 16 & 0xFF) / 255.0F,
               (float)(var5[var7] >> 8 & 0xFF) / 255.0F,
               (float)(var5[var7] & 0xFF) / 255.0F
            };
         }

         float var10 = var3 / (float)var5.length;

         for (int var8 = 1; var8 < var5.length; var8++) {
            float var9 = var8 == var5.length - 1 ? var3 : var10 * (float)var8;
            BUFFER.addVertex(var0, var1 + var9, var2, 0.0F).setColor(var6[var8][1], var6[var8][2], var6[var8][3], var6[var8][0]);
            BUFFER.addVertex(var0, var1 + var10 * (float)(var8 - 1), var2, 0.0F)
               .setColor(var6[var8 - 1][1], var6[var8 - 1][2], var6[var8 - 1][3], var6[var8 - 1][0]);
            BUFFER.addVertex(var0, var1 + var10 * (float)(var8 - 1), var2 + var4, 0.0F)
               .setColor(var6[var8 - 1][1], var6[var8 - 1][2], var6[var8 - 1][3], var6[var8 - 1][0]);
            BUFFER.addVertex(var0, var1 + var9, var2 + var4, 0.0F).setColor(var6[var8][1], var6[var8][2], var6[var8][3], var6[var8][0]);
         }
      }
   }

   public static void method_2(PoseStack var0, String var1, int var2, int var3, int var4, boolean var5) {
      Minecraft.getInstance()
         .font
         .drawInBatch(
            var1,
            (float)var2,
            (float)var3,
            var4,
            var5,
            var0.last().pose(),
            FontRenderer.field_3143.method_914().method_476(),
            DisplayMode.NORMAL,
            0,
            15728880
         );
   }

   public static void method_2(PoseStack var0, FormattedCharSequence var1, int var2, int var3, int var4, boolean var5) {
      Minecraft.getInstance()
         .font
         .drawInBatch(
            var1,
            (float)var2,
            (float)var3,
            var4,
            var5,
            var0.last().pose(),
            FontRenderer.field_3143.method_914().method_476(),
            DisplayMode.NORMAL,
            0,
            15728880
         );
   }

   public static void method_474() {
      getId(true);
   }

   public static void getId(boolean var0) {
      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);

      BUFFER.method_529();
      GlStateManager._disableBlend();
      if (var0) {
         FontRenderer.field_3143.method_474();
      }
   }

   static {
      BUFFER.method_2(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
   }
}
