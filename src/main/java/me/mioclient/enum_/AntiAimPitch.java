package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.internal.Class_0245;
import me.mioclient.module.misc.AntiAimModule;

/** AntiAim pitch pattern. */
public enum AntiAimPitch implements Nameable {
   STATIC("Static") {
      @Override
      public float method_2(AntiAimModule var1, float var2) {
         return var1.static_2.getValue();
      }
   },
   RANDOM("Random") {
      @Override
      public float method_2(AntiAimModule var1, float var2) {
         return (float)(Math.random() * 180.0 - (double)Class_0245.field_685);
      }
   };

   public final String field_2278;

    AntiAimPitch(String var3) {
      this.field_2278 = var3;
   }

   @Override
   public String getName() {
      return this.field_2278;
   }

   public abstract float method_2(AntiAimModule var1, float var2);
}
