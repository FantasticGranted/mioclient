package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.Class_0245;
import me.mioclient.module.misc.AntiAimModule;
import net.minecraft.util.Mth;

/** AntiAim yaw pattern. */
public enum AntiAimYaw implements Nameable {
   JITTER("Jitter") {
      @Override
      public float method_9(AntiAimModule var1, float var2) {
         double var3 = Math.random() * (double)var1.jitterRange.getValue().floatValue() * 2.0
            - (double)var1.jitterRange.getValue().floatValue()
            + (double)var1.base.getValue().floatValue();
         return (float)Mth.wrapDegrees(var3);
      }
   },
   SPIN("Spin") {
      @Override
      public float method_9(AntiAimModule var1, float var2) {
         var1.field_2582 = var1.field_2582 + var1.speed.getValue();
         return Mth.wrapDegrees(var1.field_2582);
      }
   },
   FLIP("Flip") {
      @Override
      public float method_9(AntiAimModule var1, float var2) {
         float var3 = var1.base.getValue() + (MinecraftAccess.mc.player.tickCount % 2 == 0 ? -var1.flip.getValue() : var1.flip.getValue());
         return Mth.wrapDegrees(var3);
      }
   },
   STATIC("Static") {
      @Override
      public float method_9(AntiAimModule var1, float var2) {
         return var1.static_.getValue();
      }
   },
   RANDOM("Random") {
      @Override
      public float method_9(AntiAimModule var1, float var2) {
         return (float)(Math.random() * (double)Class_0245.field_686);
      }
   };

   public final String field_1948;

    AntiAimYaw(String var3) {
      this.field_1948 = var3;
   }

   @Override
   public String getName() {
      return this.field_1948;
   }

   public abstract float method_9(AntiAimModule var1, float var2);
}
