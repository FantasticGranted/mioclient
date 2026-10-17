package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** ColorPickerControl button that pastes a colour from the clipboard. */
public class ColorPasteButton extends ColorPickerControl {
   public ColorPasteButton(int var1, ColorSettingWidget var2) {
      super(var1, var2, "Paste", () -> {
         try {
            Color var1x = Colors.method_209(mc.keyboardHandler.getClipboard().trim());
            var2.method_31(var1x);
            var2.method_910().method_78(var1x);
         } catch (Exception var2x) {
            var2.field_1642.reset();
            return;
         }

         var2.field_1643.reset();
      });
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (!this.field_89.method_911()) {
         Render2D.method_4(
            var2,
            (float)this.field_418.getX() + (float)this.field_418.method_216() / 2F + 0.5F,
            (float)((int)((float)(this.field_418.getY() + this.field_419) + this.method_60())),
            (float)(this.field_418.getX() + this.field_418.method_216()) - 0.5F,
            (float)(this.field_418.getY() + this.field_419) + this.method_60() + (float)this.method_66() - 0.5F,
            this.method_852().color.getValue()
         );
         super.method_2(var1, var2, var3, var5);
      }
   }

   @Override
   public float method_59() {
      return (float)this.method_167().method_216()
         - (float)this.method_167().method_216() / 4F
         - FontRenderer.field_3143.method_221(this.getName())
         + FontRenderer.field_3143.method_221(this.getName()) * 0.5F;
   }

   @Override
   public float method_60() {
      return 0.0F;
   }

   @Override
   public String getName() {
      if (!this.field_89.field_1642.method_9(1000L)) {
         return "Invalid";
      } else {
         return !this.field_89.field_1643.method_9(1000L) ? "Pasted" : super.getName();
      }
   }
}
