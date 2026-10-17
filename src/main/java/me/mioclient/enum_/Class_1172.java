package me.mioclient.enum_;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.Class_0245;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public enum Class_1172 implements MinecraftAccess, Nameable {
   field_3631("Fast", new Vec3(Class_0245.field_688, Class_0245.field_688, Class_0245.field_688)),
   field_3632("X2", new Vec3(Class_0245.field_688, 0.05, Class_0245.field_688), new Vec3(Class_0245.field_688, 1.0, Class_0245.field_688)),
   field_3633("X4", new Vec3(0.05, 0.05, 0.05), new Vec3(0.05, 0.05, 0.95), new Vec3(0.95, 0.05, 0.05), new Vec3(0.95, 0.05, 0.95)),
   field_3634(
      "X8",
      new Vec3(0.05, 0.05, 0.05),
      new Vec3(0.05, 0.05, 0.95),
      new Vec3(0.05, 0.95, 0.05),
      new Vec3(0.05, 0.95, 0.95),
      new Vec3(0.95, 0.05, 0.05),
      new Vec3(0.95, 0.05, 0.95),
      new Vec3(0.95, 0.95, 0.05),
      new Vec3(0.95, 0.95, 0.95)
   );

   public final List<Vec3> field_3635 = new ArrayList<>();
   public final String field_3636;

    Class_1172(String var3, Vec3... var4) {
      this.field_3636 = var3;
      this.field_3635.addAll(Arrays.asList(var4));
   }

   public List<Vec3> method_574() {
      return this.field_3635;
   }

   public List<Vec3> method_38(BlockPos var1) {
      return this.method_574().stream().map(var2 -> {
         var2 = var2.add(Vec3.atLowerCornerOf(var1));
         return this == field_3633 && mc.player.getEyeY() > (double)(var1.getY() + 1) ? var2.add(0.0, 0.9, 0.0) : var2;
      }).collect(Collectors.toList());
   }

   public List<Vec3> method_2(BlockPos var1, Direction var2) {
      ArrayList var3 = new ArrayList();

      for (Vec3 var5 : this.field_3635) {
         if (!this.method_2(var2.getStepX(), (int)var5.x) && !this.method_2(var2.getStepY(), (int)var5.y) && !this.method_2(var2.getStepZ(), (int)var5.z)
            )
          {
            var3.add(var5.add(Vec3.atLowerCornerOf(var1)));
         }
      }

      return var3;
   }

   public boolean method_2(int var1, int var2) {
      return (var1 | ~var2) == 1;
   }

   @Override
   public String getName() {
      return this.field_3636;
   }
}
