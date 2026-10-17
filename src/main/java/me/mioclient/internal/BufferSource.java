package me.mioclient.internal;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat.Mode;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import org.joml.Matrix4fc;

/** A VertexConsumer backed by a lazily-built BufferBuilder, with global-program draw helpers. */
public class BufferSource implements VertexConsumer {
   public static final boolean field_2556;
   public final ByteBufferBuilder field_2557;
   public BufferBuilder field_2558;
   public Mode field_2559;
   public VertexFormat field_2560;

   public static BufferSource method_740() {
      return method_34(786432);
   }

   public static BufferSource method_34(int var0) {
      return field_2556 ? Class_0971.method_374(var0) : new BufferSource(var0);
   }

   public BufferSource(int var1) {
      super();
      this.field_2557 = new ByteBufferBuilder(var1);
   }

   public BufferSource method_2(Mode var1, VertexFormat var2) {
      this.field_2559 = var1;
      this.field_2560 = var2;
      return this;
   }

   @Override
   public VertexConsumer addVertex(float x, float y, float z) {
      return this.method_741().addVertex(x, y, z);
   }

   public VertexConsumer addVertex(Matrix4fc matrix, float x, float y, float z) {
      return this.method_741().addVertex(matrix, x, y, z);
   }

   @Override
   public VertexConsumer setColor(int red, int green, int blue, int alpha) {
      return this.method_741().setColor(red, green, blue, alpha);
   }

   @Override
   public VertexConsumer setColor(int color) {
      return this.method_741().setColor(color);
   }

   @Override
   public VertexConsumer setUv(float u, float v) {
      return this.method_741().setUv(u, v);
   }

   @Override
   public VertexConsumer setUv1(int u, int v) {
      return this.method_741().setUv1(u, v);
   }

   @Override
   public VertexConsumer setUv2(int u, int v) {
      return this.method_741().setUv2(u, v);
   }

   @Override
   public VertexConsumer setNormal(float x, float y, float z) {
      return this.method_741().setNormal(x, y, z);
   }

   @Override
   public VertexConsumer setLineWidth(float var1) {
      // no-op: line width is handled by the active render pipeline in 26.1
      return this;
   }

   public boolean method_435() {
      return this.field_2558 != null;
   }

   public BufferBuilder method_9(Mode var1, VertexFormat var2) {
      this.field_2558 = new BufferBuilder(this.field_2557, var1, var2);
      return this.field_2558;
   }

   public BufferBuilder method_741() {
      if (this.field_2558 == null) {
         if (this.field_2559 == null || this.field_2560 == null) {
            throw new IllegalArgumentException("Tried to invoke empty not caching buffer");
         }

         this.field_2558 = new BufferBuilder(this.field_2557, this.field_2559, this.field_2560);
      }

      return this.field_2558;
   }

   public MeshData method_742() {
      if (this.field_2558 == null) {
         throw new IllegalArgumentException("Tried to end empty buffer");
      } else {
         MeshData var1 = this.field_2558.build();
         this.field_2558 = null;
         return var1;
      }
   }

   public void method_529() {
      if (this.field_2558 != null) {
         method_2(this.field_2558);
         this.field_2558 = null;
      }
   }

   public static void method_2(BufferSource var0) {
      if (var0.method_435()) {
         method_2(var0.field_2558);
         var0.field_2558 = null;
      }
   }

   public static void method_2(BufferBuilder var0) {
      if (var0 != null) {
         MeshData var1 = var0.build();
         if (var1 != null) {
            RenderUtil.drawMesh(var1);
         }
      }
   }

   static {
      boolean var0;
      try {
         Class.forName("net.caffeinemc.mods.sodium.api.vertex.buffer.VertexBufferWriter");
         var0 = true;
      } catch (ClassNotFoundException var2) {
         var0 = false;
      }

      field_2556 = var0;
   }
}
