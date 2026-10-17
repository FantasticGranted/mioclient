package me.mioclient.module.render;

import me.mioclient.internal.Class_0217;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.InteractionHand;
import me.mioclient.nick.Settings;

public class ViewModelModule extends Module {
   public static final String field_1905 = "°";
   public Setting<Boolean> adjust;
   public Setting<Boolean> shadow;
   public Setting<Boolean> noSway;
   public Setting<Boolean> instantSwap;
   public Setting<Boolean> eating;
   public Setting<Boolean> mainHand;
   public Setting<Boolean> arm;
   public Setting<Float> mainX;
   public Setting<Float> mainY;
   public Setting<Float> mainZ;
   public Setting<Float> mainScaleX;
   public Setting<Float> mainScaleY;
   public Setting<Float> mainScaleZ;
   public Setting<Float> mainRotateX;
   public Setting<Float> mainRotateY;
   public Setting<Float> mainRotateZ;
   public Setting<Boolean> offHand;
   public Setting<Float> offX;
   public Setting<Float> offY;
   public Setting<Float> offZ;
   public Setting<Float> offScaleX;
   public Setting<Float> offScaleY;
   public Setting<Float> offScaleZ;
   public Setting<Float> offRotateX;
   public Setting<Float> offRotateY;
   public Setting<Float> offRotateZ;
   public Setting<Boolean> misc;
   public Setting<Float> eatMultiplier;
   public Setting<Boolean> noTridentAnim;
   public Setting<Boolean> swingProgress;
   public Setting<Boolean> static_;
   public Setting<Boolean> mainHand2;
   public Setting<Float> swingProgressAmount;
   public Setting<Boolean> offHand2;
   public Setting<Float> swingProgressAmount2;
   public Setting<Boolean> viewModelFov;
   public Setting<Integer> fovAmount;

   public ViewModelModule() {
      super("ViewModel", "Transforms your 1st person view model.", Category.RENDER);
      Settings.initialize(this);
      this.mainHand2.method_31("SwingMainHand");
      this.offHand2.method_31("SwingOffHand");
      this.swingProgressAmount2.method_31("SwingProgressAmountOffHand");
      this.adjust.method_9(() -> {
         if (this.adjust.getValue()) {
            mc.setScreen(new Class_0217(this));
            this.adjust.method_78(false);
         }
      });
   }

   public float method_2(InteractionHand var1, float var2) {
      if (!this.isToggled() || !this.swingProgress.getValue()) {
         return var2;
      } else if (var1 == InteractionHand.MAIN_HAND && this.mainHand2.getValue()) {
         return Math.max(this.static_.getValue() ? 0.0F : var2, this.swingProgressAmount.getValue());
      } else {
         return var1 == InteractionHand.OFF_HAND && this.offHand2.getValue() ? Math.max(this.static_.getValue() ? 0.0F : var2, this.swingProgressAmount2.getValue()) : var2;
      }
   }
}
