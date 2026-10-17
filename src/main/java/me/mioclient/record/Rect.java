package me.mioclient.record;

/** An axis-aligned 2D rectangle (min/max X and Y) with contains, intersects, expand, and union helpers. */
public final class Rect {
   public final float field_465;
   public final float field_466;
   public final float maxX;
   public final float maxY;

   public Rect(float var1, float var2, float var3, float var4) {
      super();
      this.field_465 = var1;
      this.field_466 = var2;
      this.maxX = var3;
      this.maxY = var4;
   }

   public static Rect method_9(float var0, float var1, float var2, float var3) {
      return new Rect(var0, var1, var0 + var2, var1 + var3);
   }

   public float getWidth() {
      return this.maxX - this.field_465;
   }

   public float getHeight() {
      return this.maxY - this.field_466;
   }

   public boolean method_5(double var1, double var3) {
      return var1 >= (double)this.field_465 && var1 <= (double)this.maxX && var3 >= (double)this.field_466 && var3 <= (double)this.maxY;
   }

   public boolean method_2(Rect var1) {
      return (this.maxX < this.field_465 || this.maxX > var1.getMinX())
         && (this.maxY < this.field_466 || this.maxY > var1.getMinY())
         && (var1.getMaxX() < var1.getMinX() || var1.getMaxX() > this.field_465)
         && (var1.getMaxY() < var1.getMinY() || var1.getMaxY() > this.field_466);
   }

   public Rect method_22(float var1) {
      return this.method_7(var1, var1);
   }

   public Rect method_7(float var1, float var2) {
      return new Rect(this.getMinX() - var1, this.getMinY() - var2, this.getMaxX() + var1, this.getMaxY() + var2);
   }

   public Rect method_2(Rect... var1) {
      float var2 = this.getMinX();
      float var3 = this.getMaxX();
      float var4 = this.getMinY();
      float var5 = this.getMaxY();

      for (Rect var9 : var1) {
         var2 = Math.min(var2, var9.getMinX());
         var4 = Math.min(var4, var9.getMinY());
         var3 = Math.max(var3, var9.getMaxX());
         var5 = Math.max(var5, var9.getMaxY());
      }

      return new Rect(var2, var4, var3, var5);
   }

   public float getMinX() {
      return this.field_465;
   }

   public float getMinY() {
      return this.field_466;
   }

   public float getMaxX() {
      return this.maxX;
   }

   public float getMaxY() {
      return this.maxY;
   }
}
