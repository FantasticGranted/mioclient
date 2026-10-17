package me.mioclient.module.render;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import me.mioclient.enum_.PreType;
import me.mioclient.event.RenderEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import me.mioclient.nick.Settings;

public class GlintModule extends Module {
   public static final Identifier field_35 = Identifier.fromNamespaceAndPath("mio-mount", "textures/shine.png");
   public Setting<Color> color;

   public GlintModule() {
      super("Glint", "Changes your enchantment glint's color.", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(RenderEvent var1) {
      if (var1.method_213() == PreType.PRE) {
         Color var2 = this.color.getValue();


      } else {

      }
   }
}
