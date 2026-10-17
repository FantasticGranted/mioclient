package me.mioclient.record;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.RenderUtil;
import net.minecraft.world.phys.AABB;

/** A horizontal square area centered at (x, z) with the given size, rendered as a flat box at a chosen Y. */
public final class SquareMarker {
   public final int x;
   public final int z;
   public final int size;

   public SquareMarker(int var1, int var2, int var3) {
      super();
      this.x = var1;
      this.z = var2;
      this.size = var3;
   }

   public void method_2(PoseStack var1, Color var2, int var3, float var4) {
      double var5 = (double)var3;
      if (var3 == -1) {
         var5 = MinecraftAccess.mc.player.getPosition(RenderUtil.method_776()).y;
      }

      AABB var7 = this.method_78(var5);
      BoxRenderer.method_2(var1, var7, var2, var4);
   }

   public AABB method_78(double var1) {
      int var3 = this.size / 2;
      return new AABB(
         (double)(this.x - var3), var1, (double)(this.z - var3), (double)(this.x + var3), var1, (double)(this.z + var3)
      );
   }

   public int method_401() {
      return this.x;
   }

   public int method_791() {
      return this.z;
   }

   public int getSize() {
      return this.size;
   }
}
