package me.mioclient.clickgui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Collection;
import me.mioclient.clickgui.component.BooleanComponent;
import me.mioclient.clickgui.component.CollectionComponent;
import me.mioclient.clickgui.component.ColorComponent;
import me.mioclient.clickgui.component.EnumComponent;
import me.mioclient.clickgui.component.KeybindComponent;
import me.mioclient.clickgui.component.NumberComponent;
import me.mioclient.clickgui.component.SettingComponent;
import me.mioclient.clickgui.component.StringComponent;
import me.mioclient.clickgui.util.Animation;
import me.mioclient.clickgui.util.ColorUtil;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_26;
import me.mioclient.module.abstract_.AbstractModule_41;
import me.mioclient.module.client.UIModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/**
 * Replica of ModulePanel — one row inside a category panel.
 *
 *   left-click  → toggle module
 *   right-click → expand / collapse its settings
 *   blink      → red flash (3-step) triggered by search-Enter activation
 *
 * Width matches the parent panel; height grows when expanded, animated with the
 * same per-frame easing the original used (here unified through Animation).
 */
public class ModuleButton {
   public static final int ROW_HEIGHT = 12;

   public final Module module;
   public final Panel panel;
   public final List<SettingComponent> components = new ArrayList<>();

   private boolean expanded;
   private final Animation toggleAnim = new Animation(8F);
   private final Animation expandAnim = new Animation(10F);
   private long blinkStartMs;
   private int blinkStep;

   private static long lastHoverStartMs;
   private static ModuleButton lastHoverTarget;
   private static boolean tooltipShown;

   public ModuleButton(Panel panel, Module module) {
      this.panel  = panel;
      this.module = module;
      for (Setting<?> s : module.getRegistry()) {
         SettingComponent c = build(s);
         if (c != null) this.components.add(c);
      }
      if (!(module instanceof AbstractModule_26) && !(module instanceof AbstractModule_41)) {
         this.components.add(new KeybindComponent(this, module));
      }
   }

   private SettingComponent build(Setting<?> s) {
      Object v = s.getValue();
      if (v == null) v = s.method_99();
      if (v == null) return null;
      if (v instanceof Boolean)    return new BooleanComponent(this, cast(s));
      if (v instanceof Enum)       return new EnumComponent  (this, castEnum(s));
      if (v instanceof Number)     return new NumberComponent(this, castNum(s));
      if (v instanceof Color)      return new ColorComponent (this, castColor(s));
      if (v instanceof String)     return new StringComponent(this, castStr(s));
      if (v instanceof Collection) return new CollectionComponent(this, castColl(s));
      return null;
   }

   @SuppressWarnings({"unchecked","rawtypes"}) private static Setting<? extends Collection<?>> castColl(Setting<?> s) { return (Setting<? extends Collection<?>>) (Setting) s; }

   @SuppressWarnings("unchecked")           private static Setting<Boolean>           cast      (Setting<?> s) { return (Setting<Boolean>) s; }
   @SuppressWarnings({"unchecked","rawtypes"}) private static Setting<? extends Enum<?>>  castEnum (Setting<?> s) { return (Setting) s; }
   @SuppressWarnings({"unchecked","rawtypes"}) private static Setting<? extends Number>   castNum  (Setting<?> s) { return (Setting) s; }
   @SuppressWarnings("unchecked")           private static Setting<Color>             castColor(Setting<?> s) { return (Setting<Color>) s; }
   @SuppressWarnings("unchecked")           private static Setting<String>            castStr  (Setting<?> s) { return (Setting<String>) s; }

   public boolean isExpanded() { return this.expanded; }

   public int height() {
      this.expandAnim.target(this.expanded);
      float anim = this.expandAnim.value();
      int extra = 0;
      if (anim > 0F) {
         int full = 0;
         for (SettingComponent c : this.components) {
            if (c.isVisible()) full += c.height();
         }
         extra = Math.round(full * anim);
      }
      return ROW_HEIGHT + extra;
   }

   public void blink() {
      this.blinkStartMs = System.currentTimeMillis();
      this.blinkStep = 1;
   }

   public boolean isClosed() { return !this.expanded && this.expandAnim.value() < 0.02F; }

   public void render(GuiGraphicsExtractor ctx, int x, int y, int width, int mouseX, int mouseY) {
      Minecraft mc = Minecraft.getInstance();
      UIModule ui = UIModule.field_2843;

      this.toggleAnim.target(this.module.isToggled());
      float on = this.toggleAnim.tick();
      float ex = this.expandAnim.tick();

      // Animated row background: original slides bgEnabled from the left while
      // bgButton fills the remainder; we lerp to keep the same visual feel
      // without needing a separate scissor pass.
      int offColor = ColorUtil.argb(ui.bgButton.getValue());
      int onColor  = ColorUtil.argb(ui.bgEnabled.getValue());
      int bg = ColorUtil.lerp(offColor, onColor, on);
      ctx.fill(x + 1, y, x + width - 1, y + ROW_HEIGHT - 1, bg);

      // Blink overlay — red flash, ~150ms half-cycle, 3 cycles max
      if (this.blinkStep > 0) {
         long age = System.currentTimeMillis() - this.blinkStartMs;
         float t = (age % 300L) / 300F;
         float a = (float) Math.sin(t * Math.PI);
         ctx.fill(x + 1, y, x + width - 1, y + ROW_HEIGHT - 1,
               ColorUtil.withAlpha(0xFFFF0000, Math.round(0xC0 * a)));
         if (age >= 600L) { this.blinkStep = 0; }
      }

      int textColor = this.module.isToggled()
            ? ColorUtil.argb(ui.enabledColor.getValue())
            : ColorUtil.argb(ui.textColor.getValue());
      int textPad = Math.max(1, ui.modulePadding.getValue());
      int textY   = y + (ROW_HEIGHT - mc.font.lineHeight) / 2;

      ctx.text(mc.font, this.module.getName(), x + textPad, textY, textColor, false);

      int nameW = mc.font.width(this.module.getName());
      StringBuilder suffix = new StringBuilder();
      if (this.module.isWip()) suffix.append("beta ");
      if (ui.binds.getValue() && this.module.getKeybind().method_38() != -1) {
         suffix.append('[').append(this.module.getKeybind().method_4().toUpperCase(Locale.ROOT)).append(']');
      }
      if (suffix.length() > 0) {
         ctx.text(mc.font, suffix.toString(), x + textPad + nameW + 2, textY, textColor, false);
      }

      if (ui.gear.getValue() && !this.components.isEmpty()) {
         String pm = this.expanded ? "-" : "+";
         int pmW = mc.font.width(pm);
         ctx.text(mc.font, pm, x + width - pmW - 4, textY, textColor, false);
      }

      // Settings panel rendered inside a scissor band so the expand animation looks clean.
      if (ex > 0F) {
         int top = y + ROW_HEIGHT;
         int settingsH = 0;
         for (SettingComponent c : this.components) {
            if (c.isVisible()) settingsH += c.height();
         }
         int clipH = Math.round(settingsH * ex);
         if (clipH > 0) {
            ctx.enableScissor(x, top, x + width, top + clipH);
            int rowY = top;
            for (SettingComponent c : this.components) {
               if (!c.isVisible()) continue;
               c.render(ctx, x, rowY, width, mouseX, mouseY);
               rowY += c.height();
            }
            ctx.disableScissor();
         }
      }

      // Tooltip handling: when the row is hovered, ask the screen to set the
      // shared tooltip string after the configured delay.
      if (ui.descriptions.getValue()) {
         boolean rowHovered = within(mouseX, mouseY, x, y, width, ROW_HEIGHT);
         if (rowHovered) {
            handleHover(this);
            if (tooltipShown && this.module.getDescription() != null) {
               this.panel.screen.setTooltip(this.module.getDescription());
            }
         }
      }
   }

   private static void handleHover(ModuleButton b) {
      UIModule ui = UIModule.field_2843;
      int delay = ui.delay.getValue();
      if (delay <= 0) { tooltipShown = true; lastHoverTarget = b; return; }
      long now = System.currentTimeMillis();
      if (lastHoverTarget != b) {
         lastHoverTarget = b;
         lastHoverStartMs = now;
         tooltipShown = false;
      } else if (!tooltipShown && now - lastHoverStartMs >= delay) {
         tooltipShown = true;
      }
   }

   public static void resetHoverState() {
      lastHoverTarget  = null;
      lastHoverStartMs = 0L;
      tooltipShown     = false;
   }

   public boolean mouseClicked(double mx, double my, int button, int x, int y, int width) {
      boolean inRow = within(mx, my, x, y, width, ROW_HEIGHT);
      if (inRow) {
         if (button == 0) { this.module.method_68(); return true; }
         if (button == 1) { this.expanded = !this.expanded; return true; }
      }
      if (this.expanded) {
         int rowY = y + ROW_HEIGHT;
         for (SettingComponent c : this.components) {
            if (!c.isVisible()) continue;
            if (c.mouseClicked(mx, my, button, x, rowY, width)) return true;
            rowY += c.height();
         }
      }
      return false;
   }

   public void mouseReleased(double mx, double my, int button, int x, int y, int width) {
      if (this.expanded) {
         int rowY = y + ROW_HEIGHT;
         for (SettingComponent c : this.components) {
            if (!c.isVisible()) continue;
            c.mouseReleased(mx, my, button, x, rowY, width);
            rowY += c.height();
         }
      }
   }

   public boolean mouseScrolled(double mx, double my, double dy, int x, int y, int width) {
      if (this.expanded) {
         int rowY = y + ROW_HEIGHT;
         for (SettingComponent c : this.components) {
            if (!c.isVisible()) continue;
            if (c.mouseScrolled(mx, my, dy, x, rowY, width)) return true;
            rowY += c.height();
         }
      }
      return false;
   }

   public boolean keyPressed(int key, int scan, int mods, int x, int y, int width) {
      if (this.expanded) {
         int rowY = y + ROW_HEIGHT;
         for (SettingComponent c : this.components) {
            if (!c.isVisible()) continue;
            if (c.keyPressed(key, scan, mods, x, rowY, width)) return true;
            rowY += c.height();
         }
      }
      return false;
   }

   private static boolean within(double mx, double my, int x, int y, int w, int h) {
      return mx >= x && mx < x + w && my >= y && my < y + h;
   }
}
