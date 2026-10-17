package me.mioclient.module.client;

import java.util.concurrent.atomic.AtomicBoolean;
import me.mioclient.enum_.FontStyle;
import me.mioclient.event.Event_21;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.ClickGui;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;
import org.lwjgl.glfw.GLFW;

public class FontsModule extends Module {
   public static FontsModule field_364;
   public final AtomicBoolean field_365 = new AtomicBoolean(false);
   public static final Runnable field_366 = () -> {
      if (mc.player != null || mc.screen instanceof ClickGui) {
         FontRenderer.field_3143.method_2(FontRenderer.field_3143.method_915());
      }
   };
   public Setting<String> font;
   public Setting<FontStyle> style;
   public Setting<Boolean> antiAlias;
   public Setting<Boolean> chat;
   public Setting<Integer> size;
   public Setting<Integer> shift;
   public Setting<Float> shadow;
   public Setting<Integer> translate;

   public FontsModule() {
      super("Fonts", "Manages the client's font renderer.", Category.CLIENT);
      Settings.initialize(this);
      this.style.method_9(field_366);
      this.antiAlias.method_9(field_366);
      this.size.method_9(() -> this.field_365.set(true));
      this.font.method_9(field_366);
      field_364 = this;
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(Event_21 var1) {
      if (GLFW.glfwGetMouseButton(mc.getWindow().handle(), 0) != 1 && this.field_365.get()) {
         FontRenderer.field_3143.method_2(FontRenderer.field_3143.method_915());
         this.field_365.set(false);
      }
   }
}
