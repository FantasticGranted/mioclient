package me.mioclient.module.movement;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.MovementUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.entity.ai.attributes.Attributes;
import me.mioclient.nick.Settings;

public class HighJumpModule extends Module {
   public static final float field_878 = 0.42F;
   public Setting<Float> strength;
   public Setting<Boolean> inMovement;

   public HighJumpModule() {
      super("HighJump", "Makes you jump higher.", Category.MOVEMENT);
      Settings.initialize(this);
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         this.reset();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.inMovement.getValue() && MovementUtil.method_363()) {
         this.reset();
      } else {
         this.method_84(this.strength.getValue());
      }
   }

   public void reset() {
      this.method_84(0.42F);
   }

   public void method_84(float var1) {
      mc.player.getAttribute(Attributes.JUMP_STRENGTH).setBaseValue((double)var1);
   }
}
