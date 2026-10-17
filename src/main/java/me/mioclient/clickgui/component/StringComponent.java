package me.mioclient.clickgui.component;

import me.mioclient.clickgui.ModuleButton;
import me.mioclient.clickgui.util.ColorUtil;
import me.mioclient.module.client.UIModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** StringSettingWidget — string setting; lightweight inline edit (focus → type → enter). */
public class StringComponent extends SettingComponent {
   private boolean focused;

   public StringComponent(ModuleButton owner, Setting<String> setting) { super(owner, setting); }

   @SuppressWarnings({"unchecked","rawtypes"})
   private Setting<String> s() { return (Setting) this.setting; }

   @Override
   public void render(GuiGraphicsExtractor ctx, int x, int y, int width, int mouseX, int mouseY) {
      Minecraft mc = Minecraft.getInstance();
      UIModule ui = UIModule.field_2843;
      int color = this.focused ? ColorUtil.argb(ui.enabledColor.getValue()) : ColorUtil.argb(ui.textColor.getValue());
      ctx.text(mc.font, this.setting.getName(), x + 4, y + 2, color, false);

      String val = s().getValue();
      String display = val + (this.focused && (System.currentTimeMillis() / 500L) % 2L == 0 ? "_" : "");
      int avail = width - 8 - mc.font.width(this.setting.getName()) - 6;
      String trimmed = mc.font.plainSubstrByWidth(display, Math.max(8, avail));
      int tw = mc.font.width(trimmed);
      ctx.text(mc.font, trimmed, x + width - tw - 4, y + 2, color, false);
   }

   @Override
   public boolean mouseClicked(double mx, double my, int button, int x, int y, int width) {
      this.focused = button == 0 && within(mx, my, x, y, width, height());
      return this.focused;
   }

   @Override
   @SuppressWarnings({"unchecked","rawtypes"})
   public boolean keyPressed(int key, int scan, int mods, int x, int y, int width) {
      if (!this.focused) return false;
      if (key == 259 || key == 261) {
         String v = s().getValue();
         if (!v.isEmpty()) ((Setting) this.setting).method_78(v.substring(0, v.length() - 1));
         return true;
      }
      if (key == 257) { this.focused = false; return true; }
      return false;
   }

   @Override
   @SuppressWarnings({"unchecked","rawtypes"})
   public boolean charTyped(char ch, int mods) {
      if (!this.focused) return false;
      if (ch >= 32 && ch != 127) {
         ((Setting) this.setting).method_78(s().getValue() + ch);
         return true;
      }
      return false;
   }
}
