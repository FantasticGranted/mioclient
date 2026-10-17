package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.module.player.SpeedMineModule;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/** SpeedMine progress render mode. */
public enum SpeedMineRenderMode implements Nameable {
   PLAIN("Plain") {
      @Override
      public AABB method_2(SpeedMineModule var1, AABB var2, float var3) {
         return var2;
      }
   },
   IN("In") {
      @Override
      public AABB method_2(SpeedMineModule var1, AABB var2, float var3) {
         float var4 = 1.0F - var3;
         double var5 = Math.abs(var2.getXsize());
         double var7 = Math.abs(var2.getYsize());
         double var9 = Math.abs(var2.getZsize());
         Vec3 var11 = var2.getCenter();
         return new AABB(
            var11.subtract((double)var4 * var5 / 2.0, (double)var4 * var7 / 2.0, (double)var4 * var9 / 2.0),
            var11.add((double)var4 * var5 / 2.0, (double)var4 * var7 / 2.0, (double)var4 * var9 / 2.0)
         );
      }
   },
   OUT("Out") {
      @Override
      public AABB method_2(SpeedMineModule var1, AABB var2, float var3) {
         double var4 = Math.abs(var2.getXsize());
         double var6 = Math.abs(var2.getYsize());
         double var8 = Math.abs(var2.getZsize());
         Vec3 var10 = var2.getCenter();
         return new AABB(
            var10.subtract((double)var3 * var4 / 2.0, (double)var3 * var6 / 2.0, (double)var3 * var8 / 2.0),
            var10.add((double)var3 * var4 / 2.0, (double)var3 * var6 / 2.0, (double)var3 * var8 / 2.0)
         );
      }
   };

   public final String displayName;

    SpeedMineRenderMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public AABB method_2(SpeedMineModule var1, AABB var2, float var3) {
      return null;
   }
}
