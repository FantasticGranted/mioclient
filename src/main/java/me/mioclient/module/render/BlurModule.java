package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.ClickGui;
import me.mioclient.internal.MioFramebuffer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import me.mioclient.nick.Settings;

public class BlurModule extends Module {
   public Setting<Integer> radius;

   public BlurModule() {
      super("Blur", "Blurs the background in GUI's.", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_2(Event_26 var1) {
      this.method_7(var1.method_881());
   }

   public void method_7(GuiGraphicsExtractor var1) {
      if (!(mc.screen instanceof ClickGui var4)) {
         boolean var2 = mc.screen != null && !(mc.screen instanceof ChatScreen);
         if (var2) {
            MioFramebuffer.method_2(
               () -> var1.fill(
                     0,
                     0,
                     mc.getWindow().getGuiScaledWidth(),
                     mc.getWindow().getGuiScaledHeight(),
                     new Color(0.0F, 0.0F, 0.0F, 1F).hashCode()
                  ),
               (float)this.method_730()
            );
         }
      }
   }

   public int method_730() {
      return this.radius.getValue();
   }
}
