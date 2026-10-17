package me.mioclient.module.movement;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.VectorUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class SpiralModule extends Module {
   public Setting<Integer> field_509;
   public final List<Vec3> field_510;
   public int current;

   public SpiralModule() {
      super("Spiral", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_510 = new ArrayList<>(5000);
   }

   @Override
   public void onEnable() {
      this.current = 0;
      this.field_510.clear();
      Vec3 var1 = mc.player.position();
      float var2 = 0.0F;
      short var3 = 5000;

      for (int var4 = 0; var4 < var3 * 4; var4++) {
         this.field_510.add(var1);
         Vec3 var5 = Vec3.directionFromRotation(0.0F, var2).scale((double)(var4 * this.field_509.getValue() * 16));
         var1 = var1.add(var5);
         var2 = Mth.wrapDegrees(var2 + 9e+01F);
      }
   }

   public Vec3 method_214(int var1) {
      return this.field_510.get(var1);
   }

   @Subscribe
   public void method_5(Event_3 var1) {
      for (int var2 = Math.max(this.current, 1); var2 < Math.max(this.current, 1) + 2; var2++) {
         Vec3 var3 = this.field_510.get(var2 - 1);
         Vec3 var4 = this.field_510.get(var2);
         RenderUtil.field_2672.method_2(var1.method_10(), var3, var4, Color.white);
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      Vec3 var2 = this.method_214(this.current);
      float var3 = RotationManager.method_78(var2)[0];
      mc.player.setYRot(var3);
      if (VectorUtil.method_5(var2, mc.player.position()) < 3F) {
         this.current++;
      }
   }
}
