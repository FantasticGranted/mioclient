package me.mioclient.clickgui.component;

import java.awt.Color;
import me.mioclient.clickgui.ModuleButton;
import me.mioclient.clickgui.util.ColorUtil;
import me.mioclient.module.client.UIModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** ColorSettingWidget — color swatch with scroll-to-tweak RGB. */
public class ColorComponent extends SettingComponent {
   public ColorComponent(ModuleButton owner, Setting<Color> setting) { super(owner, setting); }

   @SuppressWarnings({"unchecked","rawtypes"})
   private Setting<Color> s() { return (Setting) this.setting; }

   @Override
   public void render(GuiGraphicsExtractor ctx, int x, int y, int width, int mouseX, int mouseY) {
      Minecraft mc = Minecraft.getInstance();
      int color = ColorUtil.argb(UIModule.field_2843.textColor.getValue());
      ctx.text(mc.font, this.setting.getName(), x + 4, y + 2, color, false);

      Color c = s().getValue();
      int sw = 7;
      int sx = x + width - sw - 4;
      int sy = y + (height() - sw) / 2;
      ctx.fill(sx, sy, sx + sw, sy + sw, ColorUtil.argb(c));
      ctx.outline(sx, sy, sw, sw, 0xFF000000);
   }

   @Override
   @SuppressWarnings({"unchecked","rawtypes"})
   public boolean mouseScrolled(double mx, double my, double dy, int x, int y, int width) {
      if (!within(mx, my, x, y, width, height())) return false;
      Color c = s().getValue();
      int step = dy > 0 ? 5 : -5;
      Color next = new Color(
            clamp(c.getRed()   + step),
            clamp(c.getGreen() + step),
            clamp(c.getBlue()  + step),
            c.getAlpha());
      ((Setting) this.setting).method_78(next);
      return true;
   }

   private static int clamp(int v) { return Math.max(0, Math.min(255, v)); }
}
