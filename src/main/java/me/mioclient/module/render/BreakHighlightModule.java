package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0421;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.MathUtil;
import me.mioclient.internal.Colors;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;

public class BreakHighlightModule extends Module {
   public Setting<Double> range;
   public Setting<Boolean> friends;
   public Setting<Boolean> enemyColor;
   public Setting<Color> fill;
   public Setting<Color> outline;

   public BreakHighlightModule() {
      super("BreakHighlight", "Highlights the blocks that are being broken.", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      for (Class_0421 var3 : Hub.field_2629.method_974()) {
         if (!(MathUtil.method_35(var3.method_111()) > this.range.getValue())
            && RotationManager.method_4(new AABB(var3.method_111()))
            && BlockUtil.method_3(var3.method_111())) {
            Color var4 = this.fill.getValue();
            Color var5 = this.outline.getValue();
            VoxelShape var6 = mc.level.getBlockState(var3.method_111()).getShape(mc.level, var3.method_111());
            float var7 = var3.method_209(var1.method_880());
            Entity var8 = var3.method_11();
            AABB var9 = this.method_2(
               var6.isEmpty()
                  ? new AABB(
                     0.0,
                     0.0,
                     0.0,
                     1.0,
                     1.0,
                     1.0
                  )
                  : var6.bounds(),
               var7
            );
            var9 = var9.move(var3.method_111());
            if (var8 instanceof Player) {
               Player var10 = (Player)var8;
               Color var11 = Hub.field_2603.method_9(var10.getGameProfile().name(), null);
               if (!this.friends.getValue() && Hub.field_2603.getId(var10)) {
                  continue;
               }

               if (var11 != null && (!Hub.field_2603.method_289(var10.getGameProfile().name()) || this.enemyColor.getValue())) {
                  var4 = Colors.method_9(var11, this.fill.getValue().getAlpha());
                  var5 = Colors.method_9(var11, this.outline.getValue().getAlpha());
               }
            }

            BoxRenderer.method_5(var1.method_10(), this.method_2(var9, var7), var4);
            BoxRenderer.method_2(var1.method_10(), this.method_2(var9, var7), var5, 1F);
         }
      }
   }

   public AABB method_2(AABB var1, float var2) {
      float var3 = Mth.clamp(var2, 0.0F, 1F);
      double var4 = Math.abs(var1.getXsize());
      double var6 = Math.abs(var1.getYsize());
      double var8 = Math.abs(var1.getZsize());
      Vec3 var10 = var1.getCenter();
      return new AABB(
         var10.subtract(
            (double)var3 * var4 / 2.0,
            (double)var3 * var6 / 2.0,
            (double)var3 * var8 / 2.0
         ),
         var10.add(
            (double)var3 * var4 / 2.0,
            (double)var3 * var6 / 2.0,
            (double)var3 * var8 / 2.0
         )
      );
   }
}
