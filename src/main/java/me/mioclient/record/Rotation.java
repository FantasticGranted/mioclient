package me.mioclient.record;

import me.mioclient.Hub;
import me.mioclient.api.BlockFilter;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.FilteredRaycastContext;
import me.mioclient.internal.RaycastBuilder;
import me.mioclient.internal.RaycastUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;

/** A view rotation (angles as {pitch, yaw}) able to raycast from the player's eyes to a hit result. */
public final class Rotation {
   public final float[] rotations;

   public Rotation(float[] var1) {
      super();
      this.rotations = var1;
   }

   public boolean method_4(BlockPos var1) {
      HitResult var2 = this.method_263();
      if (var2.getType() == Type.BLOCK) {
         AABB var3 = new AABB(
            var2.getLocation()
               .add(
                  -0.01,
                  -0.01,
                  -0.01
               ),
            var2.getLocation()
               .add(0.01, 0.01, 0.01)
         );
         return var3.intersects(new AABB(var1));
      } else {
         return false;
      }
   }

   public HitResult method_263() {
      float var1 = this.rotations[1] * Class_0245.field_690;
      float var2 = -this.rotations[0] * Class_0245.field_690;
      float var3 = Mth.cos(var2);
      float var4 = Mth.sin(var2);
      float var5 = Mth.cos(var1);
      float var6 = Mth.sin(var1);
      Vec3 var7 = new Vec3((double)(var4 * var5), (double)(-var6), (double)(var3 * var5));
      Vec3 var8 = MinecraftAccess.mc
         .player
         .getEyePosition()
         .add(
            var7.x * 5.0,
            var7.y * 5.0,
            var7.z * 5.0
         );
      FilteredRaycastContext var9 = new RaycastBuilder(MinecraftAccess.mc.player.getEyePosition(), var8)
         .method_2(BlockFilter.method_9(Hub.field_2622.method_345().keySet()))
         .method_565();
      return RaycastUtil.method_2(var9);
   }

   public float[] method_221() {
      return this.rotations;
   }
}
