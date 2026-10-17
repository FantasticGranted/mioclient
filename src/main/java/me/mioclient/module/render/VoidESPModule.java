package me.mioclient.module.render;

import java.awt.Color;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.Colors;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class VoidESPModule extends Module {
   public Setting<Float> lineWidth;
   public Setting<Float> height;
   public Setting<Integer> radius;
   public Setting<Boolean> fade;
   public Setting<Float> fadeRadius;
   public Setting<Boolean> colors;
   public Setting<Color> fill;
   public Setting<Color> line;
   public final List<BlockPos> field_138;

   public VoidESPModule() {
      super("VoidESP", "Highlights void blocks.", Category.RENDER);
      Settings.initialize(this);
      this.field_138 = new CopyOnWriteArrayList<>();
   }

   @Override
   public void onDisable() {
      this.field_138.clear();
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_138.removeIf(var0 -> InteractionUtil.getId(var0) == Blocks.BEDROCK);

      for (BlockPos var3 : BlockUtil.method_2(mc.gameRenderer.getMainCamera().position(), (float)this.radius.getValue().intValue(), false)) {
         if (var3.getY() == mc.level.getMinY() && InteractionUtil.getId(var3) != Blocks.BEDROCK && !this.field_138.contains(var3)) {
            this.field_138.add(var3);
         }
      }
   }

   @Override
   public String getInfo() {
      return String.valueOf(this.field_138.size());
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      for (BlockPos var3 : this.field_138) {
         if (RotationManager.method_4(new AABB(var3))) {
            Vec3 var4 = mc.gameRenderer.getMainCamera().position();
            if (!var3.closerToCenterThan(var4, (double)this.radius.getValue().intValue())) {
               this.field_138.remove(var3);
            } else {
               Color var5 = this.fill.getValue();
               Color var6 = this.line.getValue();
               double var7 = var4.distanceTo(var3.getCenter());
               if (this.fade.getValue() && var7 >= (double)this.fadeRadius.getValue().floatValue()) {
                  float var9 = 1F
                     - (float)Mth.clamp(
                        (var7 - (double)this.fadeRadius.getValue().floatValue())
                           / (double)((float)this.radius.getValue().intValue() - this.fadeRadius.getValue()),
                        0.0,
                        1.0
                     );
                  var5 = Colors.method_9(var5, (int)(var9 * (float)var5.getAlpha()));
                  var6 = Colors.method_9(var6, (int)(var9 * (float)var6.getAlpha()));
               }

               BoxRenderer.method_5(var1.method_10(), new AABB(var3).setMaxY((double)((float)var3.getY() + this.height.getValue())), var5);
               BoxRenderer.method_2(
                  var1.method_10(), new AABB(var3).setMaxY((double)((float)var3.getY() + this.height.getValue())), var6, this.lineWidth.getValue()
               );
            }
         }
      }
   }
}
