package me.mioclient.enum_;

import com.mojang.blaze3d.systems.RenderSystem;
import me.mioclient.api.Nameable;

/** Color-blindness simulation mode that tints rendering via a shader color. */
public enum ColorBlindMode implements Nameable {
   NORMAL("Normal"),
   PROTANOPIA("Protanopia") {
      @Override
      public float[] method_727() {
         return new float[]{0.3F, 0.2F, 1.0F, 1.0F};
      }

      @Override
      public void method_5(Runnable var1) {
         float[] var2 = this.method_727();

         var1.run();

      }
   },
   DEUTERANOPIA("Deuteranopia") {
      @Override
      public float[] method_727() {
         return new float[]{0.3F, 0.0F, 1.0F, 1.0F};
      }

      @Override
      public void method_5(Runnable var1) {
         float[] var2 = this.method_727();

         var1.run();

      }
   },
   TRITANOPIA("Tritanopia") {
      @Override
      public float[] method_727() {
         return new float[]{1.0F, 0.7F, 0.0F, 1.0F};
      }

      @Override
      public void method_5(Runnable var1) {
         float[] var2 = this.method_727();

         var1.run();

      }
   };

   public final String displayName;

    ColorBlindMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public void method_5(Runnable var1) {
      var1.run();
   }

   public float[] method_727() {
      return new float[]{1.0F, 1.0F, 1.0F, 1.0F};
   }
}
