package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.record.TimedBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class Class_0308 implements MinecraftAccess {
   public static final float field_995 = -0.0784F;
   public static final float field_996 = MovementUtil.field_1470;
   public static final float field_997 = field_996 * 0.3F;
   public static final float field_998 = field_996 * 1.4F + 0.2F;
   public final List<AABB> field_999 = new ArrayList<>();
   public final Player field_1000;

   public Class_0308(Player var1) {
      super();
      this.field_1000 = var1;
   }

   public void method_142() {
      synchronized (this.field_999) {
         this.field_999.clear();
         double var2 = this.field_1000.getX() - this.field_1000.xo;
         double var4 = this.field_1000.getY() - this.field_1000.yo;
         double var6 = this.field_1000.getZ() - this.field_1000.zo;
         double var8 = Math.hypot(var2, var6);
         double var10 = Mth.clamp(var8, this.field_1000.isShiftKeyDown() ? (double)field_997 : (double)field_996, (double)field_998);
         if (var8 != 0.0) {
            var2 = var2 / var8 * var10;
            var6 = var6 / var8 * var10;
         }

         if (EntityUtil.method_2(this.field_1000) && var4 > 0.0) {
            var4 = -0.07840000092983246;
         }

         AABB var12 = BoxUtil.method_4(this.field_1000);
         if (var4 > 0.0) {
            var4 = method_7(var4);
         }

         if (var8 < (double)MovementUtil.field_1471) {
            var2 = 0.0;
            var6 = 0.0;
         }

         if (var8 < (double)MovementUtil.field_1471 && Math.abs(var4) < 0.1) {
            this.field_999.addAll(Collections.nCopies(20, var12));
         } else {
            for (int var13 = 0; var13 <= 20; var13++) {
               boolean var14 = this.method_2(this.field_1000, var12.move(0.0, -0.01, 0.0));
               if (!var14) {
                  var4 = method_7(var4);
               } else {
                  var4 = -0.07840000092983246;
               }

               List var15 = mc.level.getEntityCollisions(this.field_1000, var12.expandTowards(var2, var4, var6));
               ArrayList var16 = new ArrayList();
               synchronized (Hub.field_2622.method_345()) {
                  for (Entry var19 : Hub.field_2622.method_345().entrySet()) {
                     TimedBlock var20 = (TimedBlock)var19.getValue();
                     var16.add(var20.getBlock().defaultBlockState().getShape(mc.level, (BlockPos)var19.getKey()));
                  }
               }

               var16.addAll(Entity.collectColliders(this.field_1000, mc.level, var15, var12.expandTowards(var2, var4, var6)));
               var12 = method_2(var12, var16, var2, var4, var6);
               this.field_999.add(var12);
            }
         }
      }
   }

   public static double method_7(double var0) {
      return (var0 + 0.08) * 0.98;
   }

   public static AABB method_2(AABB var0, List<VoxelShape> var1, double var2, double var4, double var6) {
      if (var1.isEmpty()) {
         return var0.move(var2, var4, var6);
      } else {
         AABB var8 = var0;
         if (var4 != 0.0) {
            var4 = Shapes.collide(Axis.Y, var0, var1, var4);
            if (var4 != 0.0) {
               var0 = var0.move(0.0, var4, 0.0);
            }
         }

         boolean var9 = Math.abs(var2) < Math.abs(var6);
         if (var9 && var6 != 0.0) {
            var6 = Shapes.collide(Axis.Z, var0, var1, var6);
            if (var6 != 0.0) {
               var0 = var0.move(0.0, 0.0, var6);
            }
         }

         if (var2 != 0.0) {
            var2 = Shapes.collide(Axis.X, var0, var1, var2);
            if (!var9 && var2 != 0.0) {
               var0 = var0.move(var2, 0.0, 0.0);
            }
         }

         if (!var9 && var6 != 0.0) {
            var6 = Shapes.collide(Axis.Z, var0, var1, var6);
         }

         return var8.move(var2, var4, var6);
      }
   }

   public boolean method_2(Player var1, AABB var2) {
      return mc.level.getBlockCollisions(var1, var2).iterator().hasNext();
   }

   public synchronized List<AABB> method_359() {
      return this.field_999;
   }
}
