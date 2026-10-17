package me.mioclient.internal;

/** A countdown that runs a task once its tick counter elapses. */
public class DelayedTask {
   public final Runnable field_414;
   public int field_31;

   public DelayedTask(Runnable var1, int var2) {
      super();
      this.field_414 = var1;
      this.field_31 = var2;
   }

   public Runnable method_164() {
      return this.field_414;
   }

   public int method_165() {
      return this.field_31;
   }

   public boolean method_166() {
      if (this.field_31-- < 0) {
         this.field_414.run();
         return true;
      } else {
         return false;
      }
   }
}
