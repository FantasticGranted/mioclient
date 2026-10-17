package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.MsaaLevel;
import me.mioclient.module.client.UIModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import com.mojang.blaze3d.vertex.BufferBuilder;

import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.LevelRenderer;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import net.minecraft.world.entity.Entity;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import net.minecraft.util.Mth;
import com.mojang.math.Axis;
import net.minecraft.world.phys.Vec3;
import net.minecraft.util.ARGB;
import org.joml.Matrix4f;
import org.joml.Matrix3x2fStack;
import org.lwjgl.opengl.GL20;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

/**
 * Central rendering helper: filled/outlined boxes, lines, gradients and 2D rects in world
 * space, billboarded text/textures, entity re-rendering, plus GL state and MSAA helpers.
 */
public class RenderUtil implements MinecraftAccess {
   public static final RenderUtil field_2672 = new RenderUtil();
   public static float tickDelta;
   public static PoseStack matrixStack;

   public RenderUtil() {
      super();
   }

   public static float method_776() {
      return tickDelta;
   }

   public static void method_39(float var0) {
      tickDelta = var0;
   }

   public static PoseStack method_10() {
      return matrixStack;
   }

   public static void method_2(PoseStack var0) {
      matrixStack = var0;
   }

   public void method_2(PoseStack var1, float var2, float var3, float var4, float var5, Color var6) {
      if (var4 < var2) {
         float var7 = var2;
         var2 = var4;
         var4 = var7;
      }

      if (var5 < var3) {
         float var8 = var3;
         var3 = var5;
         var5 = var8;
      }

      this.method_2(var1, var2, var4, var3, var6);
      this.method_9(var1, var4, var3, var5, var6);
      this.method_2(var1, var2, var4, var5, var6);
      this.method_9(var1, var2, var3, var5, var6);
   }

   public void method_2(PoseStack var1, float var2, float var3, float var4, float var5, Color var6, float var7) {
      this.method_2(var1, var2, var4, var3, var6, var7);
      this.method_9(var1, var4, var3, var5, var6, var7);
      this.method_2(var1, var2, var4, var5, var6, var7);
      this.method_9(var1, var2, var3, var5, var6, var7);
   }

   public void method_2(PoseStack var1, float var2, float var3, float var4, Color var5) {
      if (var3 < var2) {
         float var6 = var2;
         var2 = var3;
         var3 = var6;
      }

      this.method_9(var1, var2, var4, var3 + 1.0F, var4 + 1.0F, var5);
   }

   public void method_9(PoseStack var1, float var2, float var3, float var4, Color var5) {
      if (var4 < var3) {
         float var6 = var3;
         var3 = var4;
         var4 = var6;
      }

      this.method_9(var1, var2, var3 + 1.0F, var2 + 1.0F, var4, var5);
   }

   public void method_2(PoseStack var1, float var2, float var3, float var4, Color var5, float var6) {
      if (var3 < var2) {
         float var7 = var2;
         var2 = var3;
         var3 = var7;
      }

      this.method_9(var1, var2, var4, var3 + var6, var4 + var6, var5);
   }

   public void method_9(PoseStack var1, float var2, float var3, float var4, Color var5, float var6) {
      if (var4 < var3) {
         float var7 = var3;
         var3 = var4;
         var4 = var7;
      }

      this.method_9(var1, var2, var3 + var6, var2 + var6, var4, var5);
   }

   public void method_2(PoseStack var1, float var2, float var3, float var4, float var5, int var6) {
      if (var2 < var4) {
         float var7 = var2;
         var2 = var4;
         var4 = var7;
      }

      if (var3 < var5) {
         float var13 = var3;
         var3 = var5;
         var5 = var13;
      }

      float var8 = (float)(var6 >> 24 & 0xFF) / 255.0F;
      float var9 = (float)(var6 >> 16 & 0xFF) / 255.0F;
      float var10 = (float)(var6 >> 8 & 0xFF) / 255.0F;
      float var11 = (float)(var6 & 0xFF) / 255.0F;
      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);

      BufferBuilder var12 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      var12.addVertex(var1.last().pose(), var2, var5, 0.0F).setColor(var9, var10, var11, var8);
      var12.addVertex(var1.last().pose(), var4, var5, 0.0F).setColor(var9, var10, var11, var8);
      var12.addVertex(var1.last().pose(), var4, var3, 0.0F).setColor(var9, var10, var11, var8);
      var12.addVertex(var1.last().pose(), var2, var3, 0.0F).setColor(var9, var10, var11, var8);
      RenderUtil.drawMesh(var12.buildOrThrow());
      GlStateManager._disableBlend();
   }

   public void method_9(PoseStack var1, float var2, float var3, float var4, float var5, Color var6) {
      this.method_2(var1, var2, var3, var4, var5, var6.hashCode());
   }

   public void method_2(PoseStack var1, int var2, int var3, int var4, int var5, Color var6, Color var7) {
      GlStateManager._enableBlend();

      Tesselator var8 = Tesselator.getInstance();
      BufferBuilder var9 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      this.method_2(var1.last().pose(), var9, var2, var3, var4, var5, 0, var6.hashCode(), var7.hashCode());
      RenderUtil.drawMesh(var9.buildOrThrow());
      GlStateManager._disableBlend();
   }

   public void method_2(Matrix4f var1, VertexConsumer var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9) {
      float var10 = (float)ARGB.alpha(var8) / 255.0F;
      float var11 = (float)ARGB.red(var8) / 255.0F;
      float var12 = (float)ARGB.green(var8) / 255.0F;
      float var13 = (float)ARGB.blue(var8) / 255.0F;
      float var14 = (float)ARGB.alpha(var9) / 255.0F;
      float var15 = (float)ARGB.red(var9) / 255.0F;
      float var16 = (float)ARGB.green(var9) / 255.0F;
      float var17 = (float)ARGB.blue(var9) / 255.0F;
      var2.addVertex(var1, (float)var3, (float)var4, (float)var7).setColor(var11, var12, var13, var10);
      var2.addVertex(var1, (float)var3, (float)var6, (float)var7).setColor(var15, var16, var17, var14);
      var2.addVertex(var1, (float)var5, (float)var6, (float)var7).setColor(var15, var16, var17, var14);
      var2.addVertex(var1, (float)var5, (float)var4, (float)var7).setColor(var11, var12, var13, var10);
   }

   public int method_263(String var1) {
      return mc.font.width(var1);
   }

   public int method_2(Component var1) {
      return mc.font.width(var1);
   }

   public double method_777() {
      return (double)mc.getWindow().getGuiScaledWidth();
   }

   public double method_778() {
      return (double)mc.getWindow().getGuiScaledHeight();
   }

   public void method_2(PoseStack var1, AABB var2, Color var3) {
      float var4 = (float)(var2.minX - mc.getEntityRenderDispatcher().camera.position().x);
      float var5 = (float)(var2.minY - mc.getEntityRenderDispatcher().camera.position().y);
      float var6 = (float)(var2.minZ - mc.getEntityRenderDispatcher().camera.position().z);
      float var7 = (float)(var2.maxX - mc.getEntityRenderDispatcher().camera.position().x);
      float var8 = (float)(var2.maxY - mc.getEntityRenderDispatcher().camera.position().y);
      float var9 = (float)(var2.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
      this.method_780();

      BufferBuilder var10 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      var10.addVertex(var1.last().pose(), var4, var5, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var5, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var5, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var5, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var8, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var8, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var8, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var8, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var5, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var8, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var8, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var5, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var5, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var8, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var8, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var5, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var5, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var5, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var7, var8, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var8, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var5, var6).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var5, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var8, var9).setColor(var3.getRGB());
      var10.addVertex(var1.last().pose(), var4, var8, var6).setColor(var3.getRGB());
      RenderUtil.drawMesh(var10.buildOrThrow());
      this.method_782();
   }

   public void method_9(PoseStack var1, AABB var2, Color var3) {
      float var4 = (float)(var2.minX - mc.getEntityRenderDispatcher().camera.position().x);
      float var5 = (float)(var2.minY - mc.getEntityRenderDispatcher().camera.position().y);
      float var6 = (float)(var2.minZ - mc.getEntityRenderDispatcher().camera.position().z);
      float var7 = (float)(var2.maxX - mc.getEntityRenderDispatcher().camera.position().x);
      float var8 = (float)(var2.maxY - mc.getEntityRenderDispatcher().camera.position().y);
      float var9 = (float)(var2.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
      int var10 = var3.hashCode();
      int var11 = Colors.method_5(var3, 0);
      this.method_780();

      BufferBuilder var12 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      var12.addVertex(var1.last().pose(), var4, var5, var6).setColor(var10);
      var12.addVertex(var1.last().pose(), var7, var5, var6).setColor(var10);
      var12.addVertex(var1.last().pose(), var7, var5, var9).setColor(var10);
      var12.addVertex(var1.last().pose(), var4, var5, var9).setColor(var10);
      var12.addVertex(var1.last().pose(), var4, var5, var6).setColor(var10);
      var12.addVertex(var1.last().pose(), var4, var8, var6).setColor(var11);
      var12.addVertex(var1.last().pose(), var7, var8, var6).setColor(var11);
      var12.addVertex(var1.last().pose(), var7, var5, var6).setColor(var10);
      var12.addVertex(var1.last().pose(), var7, var5, var6).setColor(var10);
      var12.addVertex(var1.last().pose(), var7, var8, var6).setColor(var11);
      var12.addVertex(var1.last().pose(), var7, var8, var9).setColor(var11);
      var12.addVertex(var1.last().pose(), var7, var5, var9).setColor(var10);
      var12.addVertex(var1.last().pose(), var4, var5, var9).setColor(var10);
      var12.addVertex(var1.last().pose(), var7, var5, var9).setColor(var10);
      var12.addVertex(var1.last().pose(), var7, var8, var9).setColor(var11);
      var12.addVertex(var1.last().pose(), var4, var8, var9).setColor(var11);
      var12.addVertex(var1.last().pose(), var4, var5, var6).setColor(var10);
      var12.addVertex(var1.last().pose(), var4, var5, var9).setColor(var10);
      var12.addVertex(var1.last().pose(), var4, var8, var9).setColor(var11);
      var12.addVertex(var1.last().pose(), var4, var8, var6).setColor(var11);
      RenderUtil.drawMesh(var12.buildOrThrow());
      this.method_782();
   }

   public void method_2(PoseStack var1, Vec3 var2, Color var3) {
      this.method_2(var1, new AABB(var2, var2), var3);
   }

   public void method_2(PoseStack var1, BlockPos var2, Color var3) {
      this.method_2(var1, new AABB(var2), var3);
   }

   public void method_2(PoseStack var1, AABB var2, Color var3, double var4) {
      float var6 = (float)(var2.minX - mc.getEntityRenderDispatcher().camera.position().x);
      float var7 = (float)(var2.minY - mc.getEntityRenderDispatcher().camera.position().y);
      float var8 = (float)(var2.minZ - mc.getEntityRenderDispatcher().camera.position().z);
      float var9 = (float)(var2.maxX - mc.getEntityRenderDispatcher().camera.position().x);
      float var10 = (float)(var2.maxY - mc.getEntityRenderDispatcher().camera.position().y);
      float var11 = (float)(var2.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
      Tesselator var12 = Tesselator.getInstance();
      this.method_780();


      GL20.glEnable(2848);
      GL20.glHint(3154, 4354);
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
      BufferBuilder var13 = Tesselator.getInstance().begin(Mode.LINES, DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH);
      RenderUtil.mioDrawBoxLines(
         var1,
         var13,
         (double)var6,
         (double)var7,
         (double)var8,
         (double)var9,
         (double)var10,
         (double)var11,
         (float)var3.getRed() / 255.0F,
         (float)var3.getGreen() / 255.0F,
         (float)var3.getBlue() / 255.0F,
         (float)var3.getAlpha() / 255.0F
      );
      RenderUtil.drawMesh(var13.buildOrThrow());
      GL20.glDisable(2848);
      this.method_782();
   }

   public void method_2(PoseStack var1, Vec3 var2, Color var3, double var4) {
      this.method_2(var1, new AABB(var2, var2), var3, var4);
   }

   public void method_2(PoseStack var1, BlockPos var2, Color var3, double var4) {
      this.method_2(var1, new AABB(var2), var3, var4);
   }

   public void method_2(PoseStack var1, Vec3 var2, Vec3 var3, Color var4, float var5) {
      float var6 = (float)(var2.x - mc.getEntityRenderDispatcher().camera.position().x);
      float var7 = (float)(var2.y - mc.getEntityRenderDispatcher().camera.position().y);
      float var8 = (float)(var2.z - mc.getEntityRenderDispatcher().camera.position().z);
      float var9 = (float)(var3.x - mc.getEntityRenderDispatcher().camera.position().x);
      float var10 = (float)(var3.y - mc.getEntityRenderDispatcher().camera.position().y);
      float var11 = (float)(var3.z - mc.getEntityRenderDispatcher().camera.position().z);
      PoseStack.Pose var12 = var1.last();
      this.method_780();


      GL20.glEnable(2848);
      GL20.glHint(3154, 4354);
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
      BufferBuilder var13 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      var13.addVertex(var1.last().pose(), var6, var7, var8).setColor(var4.getRGB()).setNormal(var12, var5, 0.0F, 0.0F);
      var13.addVertex(var1.last().pose(), var9, var10, var11).setColor(var4.getRGB()).setNormal(var12, var5, 0.0F, 0.0F);
      var13.addVertex(var1.last().pose(), var6, var7, var8).setColor(var4.getRGB()).setNormal(var12, 0.0F, var5, 0.0F);
      var13.addVertex(var1.last().pose(), var9, var10, var11).setColor(var4.getRGB()).setNormal(var12, 0.0F, var5, 0.0F);
      var13.addVertex(var1.last().pose(), var6, var7, var8).setColor(var4.getRGB()).setNormal(var12, 0.0F, 0.0F, var5);
      var13.addVertex(var1.last().pose(), var9, var10, var11).setColor(var4.getRGB()).setNormal(var12, 0.0F, 0.0F, var5);
      RenderUtil.drawMesh(var13.buildOrThrow());
      GL20.glDisable(2848);
      this.method_782();
   }

   public void method_2(PoseStack var1, Vec3 var2, Vec3 var3, Color var4) {
      float var5 = (float)(var2.x - mc.getEntityRenderDispatcher().camera.position().x);
      float var6 = (float)(var2.y - mc.getEntityRenderDispatcher().camera.position().y);
      float var7 = (float)(var2.z - mc.getEntityRenderDispatcher().camera.position().z);
      float var8 = (float)(var3.x - mc.getEntityRenderDispatcher().camera.position().x);
      float var9 = (float)(var3.y - mc.getEntityRenderDispatcher().camera.position().y);
      float var10 = (float)(var3.z - mc.getEntityRenderDispatcher().camera.position().z);

      field_2672.method_780();
      BufferBuilder var11 = Tesselator.getInstance().begin(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
      var11.addVertex(var1.last().pose(), var5, var6, var7).setColor(var4.hashCode());
      var11.addVertex(var1.last().pose(), var8, var9, var10).setColor(var4.hashCode());
      RenderUtil.drawMesh(var11.buildOrThrow());
      field_2672.method_782();
   }

   public void method_779() {
      GlStateManager._enableBlend();
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
   }

   public void method_780() {
      this.method_779();
      GlStateManager._disableDepthTest();
      GlStateManager._depthMask(false);
      GlStateManager._disableCull();
   }

   public void method_781() {
      GlStateManager._disableBlend();
   }

   public void method_782() {
      this.method_781();
      GlStateManager._enableDepthTest();
      GlStateManager._depthMask(true);
      GlStateManager._enableCull();
   }

   public void method_38(Runnable var1) {
      if (UIModule.field_2843.mSAA.getValue() != MsaaLevel.NONE) {
         int var2 = UIModule.field_2843.mSAA.getValue().method_658();
         MsaaFramebuffer.method_2((var2 & var2 - 1) == 0 ? var2 : 2, var1);
      } else {
         var1.run();
      }
   }

   public void method_2(PoseStack var1, Vec3 var2, float var3, float var4, float var5, float var6, double var7, Color var9) {
      var1.pushPose();
      method_2(var1, var2);
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
      var1.translate(var3, var4, 0.0F);
      var1.mulPose(mc.getEntityRenderDispatcher().camera.rotation());
      var1.scale(0.025F * (float)var7, -0.025F * (float)var7, (float)(0.025F * var7));
      int var10 = (int)(var5 / 2.0F);
      GlStateManager._disableDepthTest();
      this.method_9(var1, (float)(-var10), 0.0F, (float)((int)(var5 - (float)var10)), (float)((int)var6), var9);
      GlStateManager._enableDepthTest();
      var1.popPose();
   }

   public void method_2(GuiGraphicsExtractor var1, Vec3 var2, float var3, float var4, double var5, Color var7, Identifier var8) {
      PoseStack var9 = RenderUtil.mioPose(var1.pose());
      var9.pushPose();
      method_2(var9, var2);
      Camera var10 = mc.gameRenderer.getMainCamera();
      var9.mulPose(Axis.YP.rotationDegrees(-var10.yaw()));
      var9.mulPose(Axis.XP.rotationDegrees(var10.xRot()));
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
      var9.scale(0.025F * (float)var5, -0.025F * (float)var5, 1.0F);
      int var11 = (int)(var3 / 2.0F);
      GlStateManager._disableDepthTest();
      GlStateManager._disableCull();


      var1.blit(var8, -var11, -var11, 0, 0, (float)(int)var3, (float)(int)var4, 16.0F, 16.0F);

      GlStateManager._enableDepthTest();
      var9.popPose();
   }

   public void method_9(PoseStack var1, Vec3 var2, float var3, float var4, float var5, float var6, double var7, Color var9) {
      var1.pushPose();
      method_2(var1, var2);
      Camera var10 = mc.gameRenderer.getMainCamera();
      var1.mulPose(Axis.YP.rotationDegrees(-var10.yaw()));
      var1.mulPose(Axis.XP.rotationDegrees(var10.xRot()));
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
      var1.translate(var3, var4, 0.0F);
      var1.scale(0.025F * (float)var7, -0.025F * (float)var7, 1.0F);
      int var11 = (int)(var5 / 2.0F);
      GlStateManager._disableDepthTest();
      this.method_2(var1, (float)(-var11), 0.0F, (float)((int)(var5 - (float)var11)), (float)((int)var6), var9);
      GlStateManager._enableDepthTest();
      var1.popPose();
   }

   public void method_2(GuiGraphicsExtractor var1, String var2, Vec3 var3, float var4, float var5, double var6, Color var8, boolean var9) {
      this.method_2(var1, var2, var3, var4, var5, -FontRenderer.field_3143.method_221(var2) / 2.0F, 0.0F, var6, var8, var9);
   }

   public void method_2(GuiGraphicsExtractor var1, String var2, Vec3 var3, float var4, float var5, float var6, float var7, double var8, Color var10, boolean var11) {
      var1.pose().pushMatrix();
      method_2(mioPose(var1.pose()), var3);
      GlStateManager._blendFuncSeparate(770, 771, 1, 0);
      var1.pose().translate(var4, var5);
      var1.pose().scale(0.025F * (float)var8, -0.025F * (float)var8);
      if (var11) {
         FontRenderer.field_3143.method_9(var1, var2, var6, var7, var10);
      } else {
         FontRenderer.field_3143.method_2(var1, var2, var6, var7, var10);
      }

      var1.pose().popMatrix();
   }

   public void method_2(PoseStack var1, BufferBuilder var2, Vec3 var3, Vec3 var4, int var5, int var6) {
      float var7 = (float)(var3.x - mc.getEntityRenderDispatcher().camera.position().x);
      float var8 = (float)(var3.y - mc.getEntityRenderDispatcher().camera.position().y);
      float var9 = (float)(var3.z - mc.getEntityRenderDispatcher().camera.position().z);
      float var10 = (float)(var4.x - mc.getEntityRenderDispatcher().camera.position().x);
      float var11 = (float)(var4.y - mc.getEntityRenderDispatcher().camera.position().y);
      float var12 = (float)(var4.z - mc.getEntityRenderDispatcher().camera.position().z);
      var2.addVertex(var1.last().pose(), var7, var8, var9).setColor(var5).setNormal(var1.last(), var10 - var7, var11 - var8, var12 - var9);
      var2.addVertex(var1.last().pose(), var10, var11, var12).setColor(var6).setNormal(var1.last(), var10 - var7, var11 - var8, var12 - var9);
   }

   public void method_2(PoseStack var1, BufferBuilder var2, AABB var3, int var4) {
      float var5 = (float)(var3.minX - mc.getEntityRenderDispatcher().camera.position().x);
      float var6 = (float)(var3.minY - mc.getEntityRenderDispatcher().camera.position().y);
      float var7 = (float)(var3.minZ - mc.getEntityRenderDispatcher().camera.position().z);
      float var8 = (float)(var3.maxX - mc.getEntityRenderDispatcher().camera.position().x);
      float var9 = (float)(var3.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
      Matrix4f var10 = var1.last().pose();
      var2.addVertex(var10, var5, var6, var7).setColor(var4);
      var2.addVertex(var10, var8, var6, var7).setColor(var4);
      var2.addVertex(var10, var8, var6, var9).setColor(var4);
      var2.addVertex(var10, var5, var6, var9).setColor(var4);
      var2.addVertex(var10, var5, var6, var7).setColor(var4);
   }

   public void method_2(Entity var1, float var2, PoseStack var3, MultiBufferSource var4) {
      // TODO26.1: EntityRenderDispatcher.render(...) was removed; entity rendering now
      // goes through extractEntity/submit (needs a SubmitNodeCollector at call time).
   }

   private static void mioDrawBoxLines(
      PoseStack var0,
      BufferBuilder var1,
      double x0,
      double y0,
      double z0,
      double x1,
      double y1,
      double z1,
      float r,
      float g,
      float b,
      float a
   ) {
      RenderUtil.mioBoxEdge(var0, var1, x0, y0, z0, x1, y0, z0, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y1, z0, x1, y1, z0, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y0, z1, x1, y0, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y1, z1, x1, y1, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y0, z0, x0, y1, z0, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x1, y0, z0, x1, y1, z0, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y0, z1, x0, y1, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x1, y0, z1, x1, y1, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y0, z0, x0, y0, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x1, y0, z0, x1, y0, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x0, y1, z0, x0, y1, z1, r, g, b, a);
      RenderUtil.mioBoxEdge(var0, var1, x1, y1, z0, x1, y1, z1, r, g, b, a);
   }

   private static void mioBoxEdge(
      PoseStack var0,
      BufferBuilder var1,
      double x0,
      double y0,
      double z0,
      double x1,
      double y1,
      double z1,
      float r,
      float g,
      float b,
      float a
   ) {
      PoseStack.Pose var2 = var0.last();
      double dx = x1 - x0;
      double dy = y1 - y0;
      double dz = z1 - z0;
      float len = (float)Math.sqrt(dx * dx + dy * dy + dz * dz);
      float nx = len > 0.0F ? (float)(dx / (double)len) : 0.0F;
      float ny = len > 0.0F ? (float)(dy / (double)len) : 0.0F;
      float nz = len > 0.0F ? (float)(dz / (double)len) : 0.0F;
      var1.addVertex(var2.pose(), (float)x0, (float)y0, (float)z0).setColor(r, g, b, a).setNormal(var2, nx, ny, nz);
      var1.addVertex(var2.pose(), (float)x1, (float)y1, (float)z1).setColor(r, g, b, a).setNormal(var2, nx, ny, nz);
   }

   public void method_2(Matrix4f var1, float var2, float var3, float var4, float var5, int var6, int var7, int var8, int var9) {
      float[] var10 = new float[]{(float)(var6 >> 16 & 0xFF) / 255.0F, (float)(var6 >> 8 & 0xFF) / 255.0F, (float)(var6 & 0xFF) / 255.0F};
      float[] var11 = new float[]{(float)(var7 >> 16 & 0xFF) / 255.0F, (float)(var7 >> 8 & 0xFF) / 255.0F, (float)(var7 & 0xFF) / 255.0F};
      float[] var12 = new float[]{(float)(var8 >> 16 & 0xFF) / 255.0F, (float)(var8 >> 8 & 0xFF) / 255.0F, (float)(var8 & 0xFF) / 255.0F};
      float[] var13 = new float[]{(float)(var9 >> 16 & 0xFF) / 255.0F, (float)(var9 >> 8 & 0xFF) / 255.0F, (float)(var9 & 0xFF) / 255.0F};
      this.method_779();

      BufferBuilder var14 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      var14.addVertex(var1, var2, var3 + var5, 0.0F).setColor(var13[0], var13[1], var13[2], 1.0F);
      var14.addVertex(var1, var2 + var4, var3 + var5, 0.0F).setColor(var12[0], var12[1], var12[2], 1.0F);
      var14.addVertex(var1, var2 + var4, var3, 0.0F).setColor(var11[0], var11[1], var11[2], 1.0F);
      var14.addVertex(var1, var2, var3, 0.0F).setColor(var10[0], var10[1], var10[2], 1.0F);
      RenderUtil.drawMesh(var14.buildOrThrow());
      this.method_781();
   }

   public void method_2(Matrix4f var1, float var2, float var3, float var4, float var5, int... var6) {
      if (var6.length >= 2) {
         float[][] var7 = new float[var6.length][4];

         for (int var8 = 0; var8 < var6.length; var8++) {
            var7[var8] = new float[]{
               (float)(var6[var8] >> 24 & 0xFF) / 255.0F,
               (float)(var6[var8] >> 16 & 0xFF) / 255.0F,
               (float)(var6[var8] >> 8 & 0xFF) / 255.0F,
               (float)(var6[var8] & 0xFF) / 255.0F
            };
         }

         this.method_779();

         float var12 = var4 / (float)var6.length;

         for (int var9 = 1; var9 < var6.length; var9++) {
            Tesselator var10 = Tesselator.getInstance();
            BufferBuilder var11 = Tesselator.getInstance().begin(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            var11.addVertex(var1, var2 + (var9 == var6.length - 1 ? var4 : var12 * (float)var9), var3, 0.0F)
               .setColor(var7[var9][1], var7[var9][2], var7[var9][3], var7[var9][0]);
            var11.addVertex(var1, var2 + var12 * (float)(var9 - 1), var3, 0.0F).setColor(var7[var9 - 1][1], var7[var9 - 1][2], var7[var9 - 1][3], var7[var9 - 1][0]);
            var11.addVertex(var1, var2 + var12 * (float)(var9 - 1), var3 + var5, 0.0F)
               .setColor(var7[var9 - 1][1], var7[var9 - 1][2], var7[var9 - 1][3], var7[var9 - 1][0]);
            var11.addVertex(var1, var2 + (var9 == var6.length - 1 ? var4 : var12 * (float)var9), var3 + var5, 0.0F)
               .setColor(var7[var9][1], var7[var9][2], var7[var9][3], var7[var9][0]);
            RenderUtil.drawMesh(var11.buildOrThrow());
         }

         this.method_781();
      }
   }

   public static void method_2(PoseStack var0, Vec3 var1) {
      Vec3 var2 = mc.gameRenderer.getMainCamera().position();
      double var3 = var1.x - var2.x;
      double var5 = var1.y - var2.y;
      double var7 = var1.z - var2.z;
      var0.translate(var3, var5, var7);
   }

   public static void method_9(PoseStack var0) {
      Camera var1 = mc.gameRenderer.getMainCamera();
      var0.mulPose(Axis.XP.rotationDegrees(var1.xRot()));
      var0.mulPose(Axis.YP.rotationDegrees(var1.yaw() + 180.0F));
   }

   public static void drawMesh(MeshData var0) {
      if (var0 == null) {
         return;
      } else {
         method_782(var0).draw(var0);
      }
   }

   public static RenderType method_782(MeshData var0) {
      VertexFormat var1 = var0.drawState().format();
      VertexFormat.Mode var2 = var0.drawState().mode();
      if (var1 == DefaultVertexFormat.POSITION_COLOR) {
         return var2 == VertexFormat.Mode.TRIANGLE_FAN ? RenderTypes.debugTriangleFan() : RenderTypes.debugQuads();
      } else if (var1 == DefaultVertexFormat.POSITION_COLOR_NORMAL_LINE_WIDTH) {
         return var2 == VertexFormat.Mode.LINES ? RenderTypes.linesTranslucent() : RenderTypes.lines();
      } else if (var1 == DefaultVertexFormat.ENTITY) {
         return RenderTypes.entityTranslucent(Identifier.fromNamespaceAndPath("minecraft", "textures/atlas/blocks.png"));
      } else if (var1 == DefaultVertexFormat.POSITION_TEX_COLOR) {
         return RenderTypes.outline(Identifier.fromNamespaceAndPath("minecraft", "textures/atlas/blocks.png"));
      } else if (var1 == DefaultVertexFormat.POSITION_TEX) {
         return RenderTypes.glint();
      } else {
         return RenderTypes.debugQuads();
      }
   }

   public static PoseStack mioPose(GuiGraphicsExtractor var0) {
      PoseStack var1 = new PoseStack();
      var1.mulPose(new Matrix4f().mul(var0.pose()));
      return var1;
   }

   public static PoseStack mioPose(Matrix3x2fStack var0) {
      PoseStack var1 = new PoseStack();
      var1.mulPose(new Matrix4f().mul(var0));
      return var1;
   }


    public void method_2(PoseStack var1, String var2, Vec3 var3, float var4, float var5, double var6, Color var8, boolean var9) {
       this.method_2(var1, var2, var3, var4, var5, -FontRenderer.field_3143.method_221(var2) / 2.0F, 0.0F, var6, var8, var9);
    }

    public void method_2(PoseStack var1, String var2, Vec3 var3, float var4, float var5, float var6, float var7, double var8, Color var10, boolean var11) {
       var1.pushPose();
       method_2(var1, var3);
       GlStateManager._blendFuncSeparate(770, 771, 1, 0);
       var1.translate(var4, var5, 0.0F);
       var1.mulPose(mc.getEntityRenderDispatcher().camera.rotation());
       var1.scale(0.025F * (float)var8, -0.025F * (float)var8, (float)(0.025F * var8));
       if (var11) {
          FontRenderer.field_3143.method_9(var1, var2, var6, var7, var10);
       } else {
          FontRenderer.field_3143.method_2(var1, var2, var6, var7, var10);
       }

       var1.popPose();
    }
}
