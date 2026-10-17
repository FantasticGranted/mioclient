package me.mioclient.api;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.client.UIModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** A click-GUI component with render, mouse/keyboard handlers, height metrics, and init. */
public interface GuiComponent {
   default void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
   }

   default void method_4(double var1, double var3) {
   }

   default void method_2(double var1, double var3, int var5) {
   }

   default void method_9(double var1, double var3, int var5) {
   }

   default void method_7(double var1, double var3, double var5) {
   }

   default void method_68(int var1) {
   }

   default void method_9(char var1) {
   }

   default void method_26(int var1) {
   }

   default int method_168() {
      return 0;
   }

   default void init() {
   }

   default int method_66() {
      return this.method_851();
   }

   default boolean method_5(double var1, double var3) {
      return false;
   }

   default float method_850() {
      int fh = FontRenderer.field_3143 == null ? 9 : FontRenderer.field_3143.method_66();
      return (float)(this.method_851() - fh + 1) / 2.0F;
   }

   default int method_851() {
      int fh = FontRenderer.field_3143 == null ? 9 : FontRenderer.field_3143.method_66();
      int pad = (UIModule.field_2843 == null || UIModule.field_2843.buttonHeight == null) ? 0 : UIModule.field_2843.buttonHeight.getValue();
      return 2 + fh + pad;
   }

   default UIModule method_852() {
      return UIModule.field_2843;
   }
}
