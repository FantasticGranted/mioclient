package me.mioclient.internal;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Safety;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.record.SafetyBox;
import me.mioclient.runnable.Class_1235;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ButtonBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public final class Class_0191 implements MinecraftAccess {
   public List<SafetyBox> field_536 = new ArrayList<>();
   public boolean field_537;
   public Future<?> field_538;

   public Class_0191() {
      super();
      lookups.method_14(this);
      this.field_538 = THREAD_POOL.submit(new Class_1235(this));
   }

   public boolean method_221(BlockPos var1) {
      return this.field_536.stream().anyMatch(var1x -> var1x.getBox().intersects(new AABB(var1)));
   }

   public static SafetyBox method_9(BlockPos var0, Direction var1) {
      if (!method_222(var0) && method_222(var0.below()) && !method_222(var0.above())) {
         if (mc.level.getBlockState(var0.below()).is(Blocks.END_PORTAL)) {
            return null;
         } else {
            boolean var2 = method_222(var0.above(2));
            Safety var3 = Safety.SAFE;
            Direction var4 = null;

            for (Direction var8 : Direction.values()) {
               if (var1 != var8.getOpposite() && var8 != Direction.UP) {
                  BlockPos var9 = var0.relative(var8);
                  Block var10 = mc.level.getBlockState(var9).getBlock();
                  if (!method_222(var9)) {
                     if (var4 != null || var1 != null) {
                        return null;
                     }

                     SafetyBox var11 = method_9(var0.relative(var8), var8);
                     if (var11 == null) {
                        return null;
                     }

                     if (!var11.method_676()) {
                        var2 = false;
                     }

                     if (var3 == Safety.SAFE) {
                        var3 = var11.getSafety();
                     }

                     var4 = var8;
                  } else {
                     if (var10 == Blocks.RESPAWN_ANCHOR) {
                        return null;
                     }

                     if (var10.getExplosionResistance() < 6e+02F && var10.getExplosionResistance() >= 0.0F || var10.defaultDestroyTime() == 0.0F) {
                        return null;
                     }

                     if (var10.getExplosionResistance() >= 6e+02F && var10.defaultDestroyTime() >= 0.0F) {
                        var3 = Safety.UNSAFE;
                     }
                  }
               }
            }

            return new SafetyBox(
               var3,
               var0.immutable(),
               var4 == null ? new AABB(var0) : new AABB(var0).expandTowards((double)var4.getUnitVec3i().getX(), 0.0, (double)var4.getUnitVec3i().getZ()),
               var2
            );
         }
      } else {
         return null;
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.field_538.isDone() || this.field_538.isCancelled()) {
         this.field_538 = THREAD_POOL.submit(new Class_1235(this));
      }

      this.field_537 = this.method_221(PlayerUtil.method_425());
   }

   public static boolean method_222(BlockPos var0) {
      BlockState var1 = mc.level.getBlockState(var0);
      return !var1.isAir()
         && !var1.is(Blocks.FIRE)
         && !var1.is(Blocks.SOUL_FIRE)
         && !(var1.getBlock() instanceof ButtonBlock)
         && !(var1.getBlock() instanceof TorchBlock)
         && !(var1.getBlock() instanceof RailBlock)
         && !var1.is(Blocks.LIGHT);
   }

   public List<SafetyBox> method_223() {
      return this.field_536;
   }

   public void method_39(List<SafetyBox> var1) {
      this.field_536 = var1;
   }

   public boolean method_224() {
      return this.field_537;
   }
}
