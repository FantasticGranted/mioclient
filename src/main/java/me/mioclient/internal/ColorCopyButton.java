package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** ColorPickerControl button that copies the current colour to the clipboard. */
public class ColorCopyButton extends ColorPickerControl {
   public ColorCopyButton(int var1, ColorSettingWidget var2) {
      super(var1, var2, "Copy", () -> {
         mc.keyboardHandler.setClipboard(Colors.method_2(var2.method_152(), true));
         var2.field_1644.reset();
      });
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (!this.field_89.method_911()) {
         Render2D.method_4(
            var2,
            (float)(this.field_418.getX() + 2),
            (float)((int)((float)(this.field_418.getY() + this.field_419) + this.method_60())),
            (float)this.field_418.getX() + (float)this.field_418.method_216() / 2F - 0.5F,
            (float)(this.field_418.getY() + this.field_419) + this.method_60() + (float)this.method_66() - 0.5F,
            this.method_852().color.getValue()
         );
         super.method_2(var1, var2, var3, var5);
      }
   }

   @Override
   public float method_59() {
      return (float)this.method_167().method_216() / 4F
         - FontRenderer.field_3143.method_221(this.getName()) / 2F;
   }

   @Override
   public float method_60() {
      return 0.0F;
   }

   @Override
   public String getName() {
      return !this.field_89.field_1644.method_9(1000L) ? "Copied" : super.getName();
   }
}
