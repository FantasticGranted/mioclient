package me.mioclient.internal;

import java.util.function.Supplier;
import me.mioclient.Hub;

/**
 * A float that smoothly eases toward a target value each frame (optionally with cubic
 * easing), driven by a speed supplier and the frame delta.
 */
public class AnimatedValue {
   public float current;
   public float target;
   public float from;
   public boolean eased;
   public Supplier<Float> speed;

   public AnimatedValue(float var1, boolean var2) {
      super();
      this.speed = () -> var1;
      this.eased = var2;
   }

   public AnimatedValue(float var1) {
      this(var1, true);
   }

   public AnimatedValue(Supplier<Float> var1, boolean var2) {
      super();
      this.speed = var1;
      this.eased = var2;
   }

   public float method_45() {
      float var1 = Hub.field_2601.method_154(0.5F) * this.speed.get();
      if (this.eased) {
         float var2;
         var1 = (float)(
            1.0
               - (double)(var2 = var1 - 1F) * Math.pow((double)var2, 3.0)
         );
      }

      this.current = this.from + (this.current - this.from) * var1;
      if (this.current == this.target || (double)Math.abs(this.target - this.current) < Class_0245.field_688) {
         this.method_36(this.target);
      }

      if (Hub.field_2601.method_814() <= 15) {
         this.current = this.target;
      }

      return this.current;
   }

   public void method_3(float var1) {
      if (this.current != var1) {
         this.from = this.current;
         this.current = var1;
         this.target = var1;
      }
   }

   public void method_36(float var1) {
      if (this.current != var1) {
         this.from = var1;
         this.current = var1;
         this.target = var1;
      }
   }

   public Supplier<Float> method_46() {
      return this.speed;
   }

   public void method_2(Supplier<Float> var1) {
      this.speed = var1;
   }

   public void method_22(boolean var1) {
      this.eased = var1;
   }
}
