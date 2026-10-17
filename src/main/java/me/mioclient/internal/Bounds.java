package me.mioclient.internal;

/** A simple integer rectangle (x, y and two more bounds). */
public class Bounds {
   public int field_1321;
   public int field_1322;
   public int field_1656;
   public int field_1657;

   public Bounds(int var1, int var2, int var3, int var4) {
      super();
      this.field_1321 = var1;
      this.field_1322 = var2;
      this.field_1656 = var3;
      this.field_1657 = var4;
   }

   public Bounds() {
      super();
   }

   public int getX() {
      return this.field_1321;
   }

   public int getY() {
      return this.field_1322;
   }

   public int method_216() {
      return this.field_1656;
   }

   public int method_66() {
      return this.field_1657;
   }
}
