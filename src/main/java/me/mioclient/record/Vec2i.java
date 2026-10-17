package me.mioclient.record;

/** A 2D integer point/vector with distance and addition helpers. */
public final class Vec2i {
   public final int x;
   public final int y;

   public Vec2i(int var1, int var2) {
      super();
      this.x = var1;
      this.y = var2;
   }

   public double method_2(Vec2i var1) {
      return Math.pow((double)(this.x - var1.x), 2.0)
         + Math.pow((double)(this.y - var1.y), 2.0);
   }

   public double method_9(Vec2i var1) {
      return Math.sqrt(this.method_2(var1));
   }

   public Vec2i method_5(Vec2i var1) {
      return new Vec2i(this.x + var1.x, this.y + var1.y);
   }

   public int method_401() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }
}
