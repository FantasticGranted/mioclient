package me.mioclient.record;

import me.mioclient.module.render.ViewModelModule;

/**
 * A view-model (held item) transform preset: position, scale, and rotation on X/Y/Z
 * for both the main hand and the off hand. Can be read from / applied to the ViewModel module.
 */
public final class ViewModelTransform {
   public final float mainX;
   public final float mainY;
   public final float mainZ;
   public final float offX;
   public final float offY;
   public final float offZ;
   public final float mainScaleX;
   public final float mainScaleY;
   public final float mainScaleZ;
   public final float offScaleX;
   public final float offScaleY;
   public final float offScaleZ;
   public final float mainRotateX;
   public final float mainRotateY;
   public final float mainRotateZ;
   public final float offRotateX;
   public final float offRotateY;
   public final float offRotateZ;

   public ViewModelTransform(
      float var1,
      float var2,
      float var3,
      float var4,
      float var5,
      float var6,
      float var7,
      float var8,
      float var9,
      float var10,
      float var11,
      float var12,
      float var13,
      float var14,
      float var15,
      float var16,
      float var17,
      float var18
   ) {
      super();
      this.mainX = var1;
      this.mainY = var2;
      this.mainZ = var3;
      this.offX = var4;
      this.offY = var5;
      this.offZ = var6;
      this.mainScaleX = var7;
      this.mainScaleY = var8;
      this.mainScaleZ = var9;
      this.offScaleX = var10;
      this.offScaleY = var11;
      this.offScaleZ = var12;
      this.mainRotateX = var13;
      this.mainRotateY = var14;
      this.mainRotateZ = var15;
      this.offRotateX = var16;
      this.offRotateY = var17;
      this.offRotateZ = var18;
   }

   public static ViewModelTransform method_2(ViewModelModule var0) {
      return new ViewModelTransform(
         var0.mainX.getValue(),
         var0.mainY.getValue(),
         var0.mainZ.getValue(),
         var0.offX.getValue(),
         var0.offY.getValue(),
         var0.offZ.getValue(),
         var0.mainScaleX.getValue(),
         var0.mainScaleY.getValue(),
         var0.mainScaleZ.getValue(),
         var0.offScaleX.getValue(),
         var0.offScaleY.getValue(),
         var0.offScaleZ.getValue(),
         var0.mainRotateX.getValue(),
         var0.mainRotateY.getValue(),
         var0.mainRotateZ.getValue(),
         var0.offRotateX.getValue(),
         var0.offRotateY.getValue(),
         var0.offRotateZ.getValue()
      );
   }

   public static ViewModelTransform identity() {
      return new ViewModelTransform(
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         1F,
         1F,
         1F,
         1F,
         1F,
         1F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F,
         0.0F
      );
   }

   public void method_9(ViewModelModule var1) {
      var1.mainX.method_78(this.mainX);
      var1.mainY.method_78(this.mainY);
      var1.mainZ.method_78(this.mainZ);
      var1.offX.method_78(this.offX);
      var1.offY.method_78(this.offY);
      var1.offZ.method_78(this.offZ);
      var1.mainScaleX.method_78(this.mainScaleX);
      var1.mainScaleY.method_78(this.mainScaleY);
      var1.mainScaleZ.method_78(this.mainScaleZ);
      var1.offScaleX.method_78(this.offScaleX);
      var1.offScaleY.method_78(this.offScaleY);
      var1.offScaleZ.method_78(this.offScaleZ);
      var1.mainRotateX.method_78(this.mainRotateX);
      var1.mainRotateY.method_78(this.mainRotateY);
      var1.mainRotateZ.method_78(this.mainRotateZ);
      var1.offRotateX.method_78(this.offRotateX);
      var1.offRotateY.method_78(this.offRotateY);
      var1.offRotateZ.method_78(this.offRotateZ);
   }

   public float getMainX() {
      return this.mainX;
   }

   public float getMainY() {
      return this.mainY;
   }

   public float getMainZ() {
      return this.mainZ;
   }

   public float getOffX() {
      return this.offX;
   }

   public float getOffY() {
      return this.offY;
   }

   public float getOffZ() {
      return this.offZ;
   }

   public float getMainScaleX() {
      return this.mainScaleX;
   }

   public float getMainScaleY() {
      return this.mainScaleY;
   }

   public float getMainScaleZ() {
      return this.mainScaleZ;
   }

   public float getOffScaleX() {
      return this.offScaleX;
   }

   public float getOffScaleY() {
      return this.offScaleY;
   }

   public float getOffScaleZ() {
      return this.offScaleZ;
   }

   public float getMainRotateX() {
      return this.mainRotateX;
   }

   public float getMainRotateY() {
      return this.mainRotateY;
   }

   public float getMainRotateZ() {
      return this.mainRotateZ;
   }

   public float getOffRotateX() {
      return this.offRotateX;
   }

   public float getOffRotateY() {
      return this.offRotateY;
   }

   public float getOffRotateZ() {
      return this.offRotateZ;
   }
}
