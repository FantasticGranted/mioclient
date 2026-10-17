package me.mioclient.clickgui.component;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.Locale;
import me.mioclient.clickgui.ModuleButton;
import me.mioclient.clickgui.util.ColorUtil;
import me.mioclient.module.Module;
import me.mioclient.module.client.UIModule;
import me.mioclient.record.Keybind;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** KeybindRow — keybind row. Click → wait for next key; ESC → unbind. */
public class KeybindComponent extends SettingComponent {
   private final Module module;
   private boolean listening;

   public KeybindComponent(ModuleButton owner, Module module) {
      super(owner, null);
      this.module = module;
   }

   @Override
   public boolean isVisible() { return true; }

   @Override
   public void render(GuiGraphicsExtractor ctx, int x, int y, int width, int mouseX, int mouseY) {
      Minecraft mc = Minecraft.getInstance();
      UIModule ui = UIModule.field_2843;
      int color = this.listening ? ColorUtil.argb(ui.enabledColor.getValue()) : ColorUtil.argb(ui.textColor.getValue());
      ctx.text(mc.font, "Bind", x + 4, y + 2, color, false);

      String text;
      if (this.listening) {
         text = "...";
      } else {
         int key = this.module.getKeybind().method_38();
         text = key == -1 ? "NONE" : keyName(key);
      }
      int tw = mc.font.width(text);
      ctx.text(mc.font, text, x + width - tw - 4, y + 2, color, false);
   }

   private static String keyName(int code) {
      try { return InputConstants.Type.KEYSYM.getOrCreate(code).getDisplayName().getString().toUpperCase(Locale.ROOT); }
      catch (Throwable t) { return "K" + code; }
   }

   @Override
   public boolean mouseClicked(double mx, double my, int button, int x, int y, int width) {
      if (button == 0 && within(mx, my, x, y, width, height())) {
         this.listening = true;
         return true;
      }
      return false;
   }

   @Override
   public boolean keyPressed(int key, int scan, int mods, int x, int y, int width) {
      if (!this.listening) return false;
      Keybind kb = this.module.getKeybind();
      this.module.setKeybind(kb.method_9(key == 256 ? -1 : key));
      this.listening = false;
      return true;
   }
}
