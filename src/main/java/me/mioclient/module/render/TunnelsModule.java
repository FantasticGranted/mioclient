package me.mioclient.module.render;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import java.awt.Color;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0032;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.Class_0919;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import me.mioclient.nick.Settings;

public class TunnelsModule extends Module {
   public Setting<Boolean> verticals;
   public Setting<Float> lineWidth;
   public Setting<Float> height;
   public Setting<Integer> minLength;
   public Setting<Color> fill;
   public Setting<Color> outline;
   public static final MutableBlockPos field_387 = new MutableBlockPos();
   public final Timer field_388;
   public final List<Class_0919> field_389;
   public final Queue<Class_0919> field_390;
   public final ObjectSet<Class_0919> field_391;
   public final Class_0032 field_392;

   public TunnelsModule() {
      super("Tunnels", "Highlights dug-out tunnels.", Category.RENDER);
      Settings.initialize(this);
      this.field_388 = new Timer();
      this.field_389 = Collections.synchronizedList(new ArrayList<>());
      this.field_390 = new ArrayDeque<>();
      this.field_391 = new ObjectOpenHashSet();
      this.field_392 = new Class_0032(
         1,
         (var1, var2) -> this.method_7(var1),
         (var1, var2) -> this.field_389.removeIf(var2x -> this.method_2(BlockPos.containing(var2x.field_1887.getCenter()), var1)),
         null
      );
   }

   @Override
   public void onEnable() {
      this.field_392.method_47();
      if (!this.method_535()) {
         this.field_392.method_50();
      }
   }

   @Override
   public void onDisable() {
      this.field_389.clear();
      this.field_392.method_48();
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         synchronized (this.field_389) {
            boolean var3 = !this.field_390.isEmpty();
            if (var3 && this.field_388.method_9(1000L)) {
               this.field_388.reset();
               THREAD_POOL.submit(() -> this.getId(new ArrayList<>(this.field_389)));
            }

            this.field_389
               .removeIf(
                  var0 -> Math.sqrt(mc.player.getEyePosition().distanceToSqr(var0.field_1887.getCenter()))
                        > 256.0
               );
         }
      }
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (!this.method_535()) {
         synchronized (this.field_389) {
            for (Class_0919 var4 : this.field_389) {
               if (!(var4.method_832() < (double)this.minLength.getValue().intValue())) {
                  float var5 = this.height.getValue();
                  if (var4.field_2899) {
                     var5 = (float)var4.field_1887.getYsize();
                  }

                  AABB var6 = var4.field_1887.setMaxY(var4.field_1887.minY + (double)var5);
                  if (RotationManager.method_4(var6)) {
                     BoxRenderer.method_5(var1.method_10(), var6, this.fill.getValue());
                     BoxRenderer.method_2(var1.method_10(), var6, this.outline.getValue(), this.lineWidth.getValue());
                  }
               }
            }
         }
      }
   }

   public void getId(List<Class_0919> var1) {
      while (true) {
         if (!this.field_390.isEmpty()) {
            Class_0919 var2 = this.field_390.poll();
            if (var2 != null) {
               boolean var12 = false;

               for (Class_0919 var14 : var1) {
                  if (var14.method_9(var2)) {
                     var14.method_833(BlockPos.containing(var2.field_1887.getCenter()));
                     var12 = true;
                     break;
                  }
               }

               if (!var12) {
                  var1.add(var2);
               }
               continue;
            }
         }

         for (int var8 = 0; var8 < var1.size(); var8++) {
            for (int var3 = var8; var3 < var1.size(); var3++) {
               Class_0919 var4 = (Class_0919)var1.get(var8);
               Class_0919 var5 = (Class_0919)var1.get(var3);
               if (var4.method_9(var5)) {
                  this.field_391.add(var5);
               }
            }
         }

         ObjectIterator var9 = this.field_391.iterator();

         while (var9.hasNext()) {
            Class_0919 var11 = (Class_0919)var9.next();
            var1.remove(var11);
         }

         this.field_391.clear();
         synchronized (this.field_389) {
            this.field_389.clear();
            this.field_389.addAll(var1);
            var1.clear();
            return;
         }
      }
   }

   public void method_7(ChunkPos var1) {
      for (int var2 = 0; var2 < 16; var2++) {
         for (int var3 = mc.level.getMinY(); var3 < mc.level.getMaxY(); var3++) {
            for (int var4 = 0; var4 < 16; var4++) {
               field_387.set(var1.getMinBlockX() + var2, var3, var1.getMinBlockZ() + var4);
               if (this.method_152(field_387)) {
                  boolean var5 = this.method_151(field_387);
                  if (var5 || this.method_150(field_387)) {
                     if (var5) {
                        this.field_390.add(new Class_0919(field_387, true));
                     } else {
                        for (Direction var7 : Plane.HORIZONTAL) {
                           BlockPos var8 = field_387.relative(var7);
                           if (this.method_150(var8)) {
                              this.field_390.add(new Class_0919(field_387, false));
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public boolean method_150(BlockPos var1) {
      if (!this.method_152(var1.above()) || this.method_152(var1.below()) || this.method_152(var1.above().above())) {
         return false;
      } else if (this.method_152(var1.north()) && this.method_152(var1.south()) && this.method_152(var1.above().north()) && this.method_152(var1.above().south())) {
         return !this.method_152(var1.east()) && !this.method_152(var1.west()) && !this.method_152(var1.above().east()) && !this.method_152(var1.above().west());
      } else {
         return this.method_152(var1.east()) && this.method_152(var1.west()) && this.method_152(var1.above().east()) && this.method_152(var1.above().west())
            ? !this.method_152(var1.north()) && !this.method_152(var1.south()) && !this.method_152(var1.above().north()) && !this.method_152(var1.above().south())
            : false;
      }
   }

   public boolean method_151(BlockPos var1) {
      if (!this.verticals.getValue()) {
         return false;
      } else {
         for (Direction var3 : Plane.HORIZONTAL) {
            BlockPos var4 = var1.relative(var3);
            BlockState var5 = mc.level.getBlockState(var4);
            if (!var5.isSolid() || var5.is(Blocks.BAMBOO_BLOCK) || var5.is(Blocks.BASALT)) {
               return false;
            }
         }

         for (Direction var7 : Plane.VERTICAL) {
            BlockPos var8 = var1.relative(var7);
            if (!this.method_152(var8) || !this.method_152(var8.relative(var7))) {
               return false;
            }
         }

         return true;
      }
   }

   public boolean method_2(BlockPos var1, ChunkPos var2) {
      return var1.getX() >= var2.getMinBlockX() && var1.getX() <= var2.getMaxBlockX() && var1.getZ() >= var2.getMinBlockZ() && var1.getZ() <= var2.getMaxBlockZ();
   }

   public boolean method_152(BlockPos var1) {
      return !mc.level.getBlockState(var1).isSolid();
   }
}
