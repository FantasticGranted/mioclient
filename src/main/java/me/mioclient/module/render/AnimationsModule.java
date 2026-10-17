package me.mioclient.module.render;

import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class AnimationsModule extends Module {
   public Setting<Boolean> crystals;
   public Setting<Float> floatFactor;
   public Setting<Float> rotationSpeed;
   public Setting<Float> crystalScale;
   public Setting<Boolean> parts;
   public Setting<Boolean> inner;
   public Setting<Boolean> outer;
   public Setting<Boolean> core;
   public Setting<Boolean> bottom;
   public Setting<Boolean> players;
   public Setting<Boolean> static_;
   public Setting<Boolean> sneak;
   public Setting<Float> playerScale;

   public AnimationsModule() {
      super("Animations", "Modifies entity animations.", Category.RENDER);
      Settings.initialize(this);
   }

   public boolean method_128() {
      return this.isToggled() && this.players.getValue() && this.playerScale.getValue() != 1F;
   }

   public boolean method_129() {
      return this.isToggled() && this.players.getValue() && this.static_.getValue();
   }

   public boolean method_130() {
      return this.isToggled() && this.players.getValue() && this.sneak.getValue();
   }

   public boolean method_131() {
      return this.isToggled() && this.crystals.getValue();
   }

   public boolean method_132(int var1) {
      return switch (var1) {
         case 0 -> this.method_136();
         case 1 -> this.method_134();
         case 2 -> this.method_133();
         case 3 -> this.method_135();
         default -> false;
      };
   }

   public boolean method_133() {
      return this.method_131() && !this.inner.getValue();
   }

   public boolean method_134() {
      return this.method_131() && !this.outer.getValue();
   }

   public boolean method_135() {
      return this.method_131() && !this.core.getValue();
   }

   public boolean method_136() {
      return this.method_131() && !this.bottom.getValue();
   }
}
