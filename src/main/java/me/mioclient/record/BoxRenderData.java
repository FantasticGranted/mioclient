package me.mioclient.record;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

/** Captured render state for a box: position/normal matrices, the Box, color, and line width. */
public final class BoxRenderData {
   public final Matrix4f positionMatrix;
   public final Matrix3f normalMatrix;
   public final AABB field_1887;
   public final Color color;
   public final float field_1889;

   public BoxRenderData(Matrix4f var1, Matrix3f var2, AABB var3, Color var4, float var5) {
      super();
      this.positionMatrix = var1;
      this.normalMatrix = var2;
      this.field_1887 = var3;
      this.color = var4;
      this.field_1889 = var5;
   }

   public static BoxRenderData of(PoseStack var0, AABB var1, Color var2, float var3) {
      return new BoxRenderData(new Matrix4f(var0.last().pose()), new Matrix3f(var0.last().normal()), var1, var2, var3);
   }

   public Matrix4f getPositionMatrix() {
      return this.positionMatrix;
   }

   public Matrix3f getNormalMatrix() {
      return this.normalMatrix;
   }

   public AABB getBox() {
      return this.field_1887;
   }

   public Color getColor() {
      return this.color;
   }

   public float getLineWidth() {
      return this.field_1889;
   }
}
