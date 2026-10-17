package me.mioclient.internal;

import java.util.function.Supplier;
import me.mioclient.Hub;
import net.minecraft.util.Mth;

/**
 * An {@link AnimatedValue} specialized for 0..1 toggle transitions (expand/collapse), using
 * an offset range and cubic ease-out so it animates smoothly between on and off.
 */
public class ToggleAnimation extends AnimatedValue {
   public ToggleAnimation(float var1, boolean var2) {
      super(var1, var2);
   }

   public ToggleAnimation(float var1) {
      super(var1);
   }

   public ToggleAnimation(Supplier<Float> var1, boolean var2) {
      super(var1, var2);
   }

   @Override
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
      if (this.current == this.target || (double)Math.abs(this.target - this.current) < 0.01) {
         this.method_36(this.target);
      }

      if (Hub.field_2601.method_814() <= 15) {
         this.current = this.target;
      }

      return Mth.clamp(this.current - 1F, 0.0F, 1F);
   }

   @Override
   public void method_3(float var1) {
      super.method_3(var1 + 1F);
   }

   public float method_605() {
      return this.current;
   }

   public void method_3(boolean var1) {
      this.method_3(var1 ? 1F : 0.0F);
   }

   public void method_36(boolean var1) {
      this.method_36(var1 ? 2F : 1F);
   }

   public void reset() {
      this.method_3(0.0F);
   }
}
