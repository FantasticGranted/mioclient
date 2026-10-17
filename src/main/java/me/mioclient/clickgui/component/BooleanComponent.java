package me.mioclient.clickgui.component;

import me.mioclient.clickgui.ModuleButton;
import me.mioclient.clickgui.util.ColorUtil;
import me.mioclient.module.client.UIModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** BooleanSettingWidget in the original — boolean rendered as text + colored dot. */
public class BooleanComponent extends SettingComponent {
   @SuppressWarnings("unchecked")
   private Setting<Boolean> s() { return (Setting<Boolean>) this.setting; }

   public BooleanComponent(ModuleButton owner, Setting<Boolean> setting) { super(owner, setting); }

   @Override
   public void render(GuiGraphicsExtractor ctx, int x, int y, int width, int mouseX, int mouseY) {
      UIModule ui = UIModule.field_2843;
      Minecraft mc = Minecraft.getInstance();
      boolean v = s().getValue();
      int color = v ? ColorUtil.argb(ui.enabledColor.getValue()) : ColorUtil.argb(ui.textColor.getValue());
      ctx.text(mc.font, this.setting.getName(), x + 4, y + 2, color, false);
      int dot = 4;
      ctx.fill(x + width - dot - 4, y + (ROW_HEIGHT - dot) / 2, x + width - 4, y + (ROW_HEIGHT - dot) / 2 + dot, color);
   }

   @Override
   public boolean mouseClicked(double mx, double my, int button, int x, int y, int width) {
      if (button == 0 && within(mx, my, x, y, width, height())) {
         s().method_78(!s().getValue());
         return true;
      }
      return false;
   }
}
