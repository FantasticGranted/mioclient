package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.awt.Color;
import java.util.Comparator;
import java.util.List;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.record.BoxRenderData;
import net.minecraft.client.renderer.GameRenderer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL32C;
import com.mojang.blaze3d.opengl.GlStateManager;

/**
 * World-space box ESP renderer: batches filled boxes and outlines (with per-box line width,
 * depth offset) into shared buffers and flushes them on the render event.
 */
public final class BoxRenderer implements MinecraftAccess {
   public static final BufferSource fillBuffer = BufferSource.method_740();
   public static final BufferSource lineBuffer = BufferSource.method_740();
   public static final List<BoxRenderData> boxes = new ObjectArrayList();

   public BoxRenderer() {
      super();
   }

   @Subscribe(
      method_800 = -9999
   )
   public static void method_2(Event_3 var0) {
      method_474();
      GlStateManager._enablePolygonOffset();
      GlStateManager._polygonOffset(1.0F, -1500000.0F);
      GL20.glDepthRange(0.0, 0.1);
      Render2D.method_474();
      GL20.glDepthRange(0.0, 1.0);
      GlStateManager._polygonOffset(1.0F, 1500000.0F);
      GlStateManager._disablePolygonOffset();
   }

   public static void method_9(PoseStack var0, BlockPos var1, Color var2) {
      method_5(var0, new AABB(var1), var2);
   }

   public static void method_5(PoseStack var0, AABB var1, Color var2) {
      if (var2.getAlpha() != 0) {
         float var3 = (float)(var1.minX - mc.getEntityRenderDispatcher().camera.position().x);
         float var4 = (float)(var1.minY - mc.getEntityRenderDispatcher().camera.position().y);
         float var5 = (float)(var1.minZ - mc.getEntityRenderDispatcher().camera.position().z);
         float var6 = (float)(var1.maxX - mc.getEntityRenderDispatcher().camera.position().x);
         float var7 = (float)(var1.maxY - mc.getEntityRenderDispatcher().camera.position().y);
         float var8 = (float)(var1.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
         int var9 = method_4(var2).hashCode();
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var5).setColor(var9);
      }
   }

   public static void method_7(PoseStack var0, AABB var1, Color var2) {
      if (var2.getAlpha() != 0) {
         float var3 = (float)(var1.minX - mc.getEntityRenderDispatcher().camera.position().x);
         float var4 = (float)(var1.minY - mc.getEntityRenderDispatcher().camera.position().y);
         float var5 = (float)(var1.minZ - mc.getEntityRenderDispatcher().camera.position().z);
         float var6 = (float)(var1.maxX - mc.getEntityRenderDispatcher().camera.position().x);
         float var7 = (float)(var1.maxY - mc.getEntityRenderDispatcher().camera.position().y);
         float var8 = (float)(var1.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
         int var9 = var2.hashCode();
         int var10 = Colors.method_5(var2, 0);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var5).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var5).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var5).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var8).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var6, var7, var8).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var8).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var5).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var4, var8).setColor(var9);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var8).setColor(var10);
         fillBuffer.addVertex(var0.last().pose(), var3, var7, var5).setColor(var10);
      }
   }

   public static void method_2(PoseStack var0, BlockPos var1, Color var2, float var3) {
      method_2(var0, new AABB(var1), var2, var3);
   }

   public static void method_2(PoseStack var0, AABB var1, Color var2, float var3) {
      if (var2.getAlpha() != 0) {
         if (!(var3 <= 0.0F)) {
            boxes.add(BoxRenderData.of(var0, var1, method_4(var2), var3));
         }
      }
   }

   public static void method_2(BoxRenderData var0) {
      Matrix4f var1 = var0.positionMatrix;
      Matrix3f var2 = var0.normalMatrix;
      float var3 = (float)(var0.field_1887.minX - mc.getEntityRenderDispatcher().camera.position().x);
      float var4 = (float)(var0.field_1887.minY - mc.getEntityRenderDispatcher().camera.position().y);
      float var5 = (float)(var0.field_1887.minZ - mc.getEntityRenderDispatcher().camera.position().z);
      float var6 = (float)(var0.field_1887.maxX - mc.getEntityRenderDispatcher().camera.position().x);
      float var7 = (float)(var0.field_1887.maxY - mc.getEntityRenderDispatcher().camera.position().y);
      float var8 = (float)(var0.field_1887.maxZ - mc.getEntityRenderDispatcher().camera.position().z);
      int var9 = var0.color.hashCode();
      lineBuffer.addVertex(var1, var3, var4, var5).setColor(var9);
      lineBuffer.addVertex(var1, var6, var4, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var4, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var7, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var4, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var4, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var4, var5).setColor(var9);
      lineBuffer.addVertex(var1, var6, var7, var5).setColor(var9);
      lineBuffer.addVertex(var1, var6, var7, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var7, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var7, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var7, var8).setColor(var9);
      lineBuffer.addVertex(var1, var3, var7, var8).setColor(var9);
      lineBuffer.addVertex(var1, var3, var4, var8).setColor(var9);
      lineBuffer.addVertex(var1, var3, var4, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var4, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var4, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var4, var5).setColor(var9);
      lineBuffer.addVertex(var1, var3, var7, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var7, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var4, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var7, var8).setColor(var9);
      lineBuffer.addVertex(var1, var6, var7, var5).setColor(var9);
      lineBuffer.addVertex(var1, var6, var7, var8).setColor(var9);
   }

   public static Color method_4(Color var0) {
      // TODO26.1: RenderSystem.getShaderFog() now returns an opaque GpuBufferSlice; fog color readback removed.
      return var0;
   }

   public static Color method_2(Color var0, float[] var1) {
      return new Color(
         (int)((float)var0.getRed() * var1[0]),
         (int)((float)var0.getGreen() * var1[1]),
         (int)((float)var0.getBlue() * var1[2]),
         (int)((float)var0.getAlpha() * var1[3])
      );
   }

   public static void method_474() {
      RenderUtil.field_2672.method_780();

      fillBuffer.method_529();
      boxes.sort(Comparator.comparing(var0x -> var0x.field_1889));
      float var0 = 0.0F;

      for (BoxRenderData var2 : boxes) {
         if (var2.field_1889 != var0) {
            method_623();
         }

         var0 = var2.field_1889;
         GL32C.glLineWidth(var0);
         method_2(var2);
      }

      boxes.clear();
      method_623();
      RenderUtil.field_2672.method_782();
   }

   public static void method_623() {
      lineBuffer.method_529();
      GL32C.glLineWidth(1.0F);
   }

   static {
      lookups.method_5(BoxRenderer.class);
      fillBuffer.method_2(Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
      lineBuffer.method_2(Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
   }
}
