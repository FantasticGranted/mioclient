package me.mioclient.module.render;

import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ToggleAnimation;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class ViewClipModule extends Module {
   public Setting<Double> range;
   public Setting<Boolean> smooth;
   public final ToggleAnimation field_4496;

   public ViewClipModule() {
      super("ViewClip", "Allows you to clip into blocks using 3rd person camera.", Category.RENDER);
      Settings.initialize(this);
      this.field_4496 = new ToggleAnimation(1.3F, true);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (mc.gameRenderer.getMainCamera().isDetached()) {
         this.field_4496.method_3(true);
      } else {
         this.field_4496.method_36(1F);
      }
   }
}
