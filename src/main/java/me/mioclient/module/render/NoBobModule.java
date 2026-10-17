package me.mioclient.module.render;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class NoBobModule extends Module {
   public Setting<Float> multiplier;

   public NoBobModule() {
      super("NoBob", "Modifies the bobbing animation.", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         if (this.multiplier.getValue() <= 0.0F) {
            mc.player.avatarState().walkDist = 0.0F;
         }
      }
   }
}
