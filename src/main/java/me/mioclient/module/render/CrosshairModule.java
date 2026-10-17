package me.mioclient.module.render;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ToggleAnimation;
import me.mioclient.internal.Render2D;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class CrosshairModule extends Module {
   public Setting<Boolean> thirdPerson;
   public Setting<Boolean> dot;
   public Setting<Boolean> shadow;
   public Setting<Integer> distance;
   public Setting<Boolean> dynamic;
   public Setting<Boolean> smooth;
   public Setting<Float> amplitude;
   public Setting<Integer> gap;
   public Setting<Integer> length;
   public Setting<Integer> width;
   public Setting<Color> color;
   public final ToggleAnimation field_3512;

   public CrosshairModule() {
      super("Crosshair", "Allows you to customize your crosshair.", Category.RENDER);
      Settings.initialize(this);
      this.field_3512 = new ToggleAnimation(2F, true);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(Event_26 var1) {
      if (!this.method_535() && (!mc.gameRenderer.getMainCamera().isDetached() || this.thirdPerson.getValue())) {
         float var2 = (float)this.gap.getValue().intValue();
         if (this.dynamic.getValue()) {
            if (this.smooth.getValue()) {
               this.field_3512
                  .method_3(
                     mc.player.input.moveVector.y != 0.0F || mc.player.input.moveVector.x != 0.0F || mc.player.input.keyPresses.jump()
                  );
            } else {
               this.field_3512
                  .method_36(
                     mc.player.input.moveVector.y != 0.0F || mc.player.input.moveVector.x != 0.0F || mc.player.input.keyPresses.jump()
                  );
            }

            var2 += this.amplitude.getValue() * this.field_3512.method_45();
         }

         float var3 = (float)mc.getWindow().getGuiScale();
         var1.method_10().pushPose();
         var1.method_10().scale(1F / var3, 1F / var3, 1F / var3);
         if (this.shadow.getValue()) {
            PoseStack var4 = var1.method_10();
            var4.pushPose();
            var4.translate((float)this.distance.getValue().intValue(), (float)this.distance.getValue().intValue(), 0.0F);
            this.method_5(
               var1.method_10(),
               (float)this.length.getValue().intValue(),
               (float)this.width.getValue().intValue() / 2F,
               var2,
               Color.BLACK
            );
            var4.popPose();
         }

         this.method_5(
            var1.method_10(),
            (float)this.length.getValue().intValue(),
            (float)this.width.getValue().intValue() / 2F,
            var2,
            this.color.getValue()
         );
         var1.method_10().popPose();
      }
   }

   public void method_5(PoseStack var1, float var2, float var3, float var4, Color var5) {
      int var6 = mc.getWindow().getScreenWidth() / 2;
      int var7 = mc.getWindow().getScreenHeight() / 2;
      if (Math.ceil((double)var3) - (double)var3 != 0.0) {
         var4 += 0.5F;
      } else {
         var6--;
         var7++;
      }

      if (this.dot.getValue()) {
         Render2D.method_4(var1, (float)var6 - var3, (float)var7 - var3, (float)var6 + var3, (float)var7 + var3, var5);
      }

      if (var2 != 0.0F) {
         Render2D.method_4(var1, (float)var6 - var3, (float)var7 - var4 - var2, (float)var6 + var3, (float)var7 - var4, var5);
         Render2D.method_4(var1, (float)var6 + var4, (float)var7 - var3, (float)var6 + var4 + var2, (float)var7 + var3, var5);
         Render2D.method_4(var1, (float)var6 - var3, (float)var7 + var4, (float)var6 + var3, (float)var7 + var4 + var2, var5);
         Render2D.method_4(var1, (float)var6 - var4 - var2, (float)var7 - var3, (float)var6 - var4, (float)var7 + var3, var5);
      }
   }
}
