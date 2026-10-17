package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;

/** A world-space Box with a timestamp and alpha, for time-faded box rendering. */
public class FadingBox {
   public AABB field_3523;
   public long field_677;
   public float field_1889 = 1F;

   public FadingBox() {
      super();
   }

   public void method_1016() {
      this.field_677 = System.currentTimeMillis() + 150L;
   }

   public void method_425(BlockPos var1) {
      this.field_3523 = new AABB(var1);
   }

   public void method_29(AABB var1) {
      this.field_3523 = var1;
   }

   public void method_17(float var1) {
      this.field_1889 = var1;
   }

   public void method_2(PoseStack var1, Color var2, Color var3, float var4, boolean var5) {
      if (this.field_3523 != null && RotationManager.method_4(this.field_3523)) {
         float var6 = this.method_37(var4);
         BoxRenderer.method_5(var1, this.field_3523, var5 ? Colors.method_9(var2, (int)((float)var2.getAlpha() * var6)) : var2);
         BoxRenderer.method_2(var1, this.field_3523, var5 ? Colors.method_9(var3, (int)((float)var3.getAlpha() * var6)) : var3, this.field_1889);
         if (var6 == 0.0F) {
            this.field_3523 = null;
         }
      }
   }

   public float method_37(float var1) {
      return Mth.clamp(
         1F - (float)Math.max(System.currentTimeMillis() - this.field_677, 0L) / var1, 0.0F, 1F
      );
   }

   public AABB method_1017() {
      return this.field_3523;
   }
}
