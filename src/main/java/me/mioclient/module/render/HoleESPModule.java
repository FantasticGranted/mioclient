package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.enum_.Safety;
import me.mioclient.enum_.HoleFillMode;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.SafetyBox;
import me.mioclient.setting.Setting;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class HoleESPModule extends Module {
   public Setting<HoleFillMode> fill;
   public Setting<Float> lineWidth;
   public Setting<Float> height;
   public Setting<Integer> radius;
   public Setting<Boolean> hideOwn;
   public Setting<Boolean> fade;
   public Setting<Float> fadeRadius;
   public Setting<Boolean> safe;
   public Setting<Color> safeFill;
   public Setting<Color> safeOutline;
   public Setting<Boolean> unsafe;
   public Setting<Color> unsafeFill;
   public Setting<Color> unsafeOutline;
   public Setting<Boolean> trapped;
   public Setting<Color> trappedFill;
   public Setting<Color> trappedOutline;

   public HoleESPModule() {
      super("HoleESP", "Highlights the spots that are safe from end crystals.", Category.RENDER);
      Settings.initialize(this);
   }

   @Override
   public String getInfo() {
      return String.valueOf(Hub.field_2605.method_223().size());
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      double var2 = Mth.lerp((double)var1.method_880(), mc.player.yo, mc.player.getY());
      Vec3 var4 = mc.gameRenderer.getMainCamera().position();
      AABB var5 = new AABB(PlayerUtil.method_425());

      for (SafetyBox var7 : Hub.field_2605.method_223()) {
         if (RotationManager.method_4(var7.getBox())
            && (this.trapped.getValue() || !var7.method_676())
            && var7.getPos().closerToCenterThan(var4, (double)this.radius.getValue().intValue())
            && (!mc.player.getBoundingBox().intersects(var7.getBox()) || !this.hideOwn.getValue() || this.fade.getValue())) {
            Color var8 = this.safeFill.getValue();
            Color var9 = this.safeOutline.getValue();
            if (var7.method_676()) {
               var8 = this.trappedFill.getValue();
               var9 = this.trappedOutline.getValue();
            } else if (var7.getSafety() == Safety.UNSAFE) {
               var8 = this.unsafeFill.getValue();
               var9 = this.unsafeOutline.getValue();
            }

            double var10 = var4.distanceTo(var7.getPos().getCenter());
            boolean var12 = var7.getBox().intersects(var5) && this.hideOwn.getValue();
            if (this.fade.getValue() && (var10 >= (double)this.fadeRadius.getValue().floatValue() || var12)) {
               float var13 = 1F
                  - (float)Mth.clamp(
                     (var10 - (double)this.fadeRadius.getValue().floatValue())
                        / (double)((float)this.radius.getValue().intValue() - this.fadeRadius.getValue()),
                     0.0,
                     1.0
                  );
               if (var12) {
                  var13 = (float)(var2 - Math.floor(var2));
               }

               var8 = Colors.method_9(var8, (int)(var13 * (float)var8.getAlpha()));
               var9 = Colors.method_9(var9, (int)(var13 * (float)var9.getAlpha()));
            }

            switch ((HoleFillMode)this.fill.getValue()) {
               case SOLID:
                  BoxRenderer.method_5(
                     var1.method_10(), var7.getBox().setMaxY((double)((float)var7.getPos().getY() + this.height.getValue())), var8
                  );
                  break;
               case GRADIENT:
                  BoxRenderer.method_7(var1.method_10(), var7.getBox().setMaxY((double)(var7.getPos().getY() + 1)), var8);
            }

            BoxRenderer.method_2(
               var1.method_10(),
               var7.getBox().setMaxY((double)((float)var7.getPos().getY() + this.height.getValue())),
               var9,
               this.lineWidth.getValue()
            );
         }
      }
   }
}
