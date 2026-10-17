package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.function.Supplier;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** ClickGui clickable label: a LabelComponent that runs a Runnable when clicked. */
public class ButtonComponent extends LabelComponent {
   public final Runnable field_3980;

   public ButtonComponent(ClickGuiPanel var1, String var2, Runnable var3) {
      this(var1, () -> var2, var3);
   }

   public ButtonComponent(ClickGuiPanel var1, Supplier<String> var2, Runnable var3) {
      super(var1, var2);
      this.field_3980 = var3;
      this.method_460(true);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.method_949()) {
         Render2D.method_4(
            var2,
            (float)(this.field_418.getX() + 1),
            (float)(this.field_418.getY() + this.field_419) + 0.5F,
            (float)(this.field_418.getX() + this.field_418.method_216() - 1),
            (float)(this.field_418.getY() + this.field_419 + this.method_66()) - 0.5F,
            this.method_852().color.getValue()
         );
      }

      super.method_2(var1, var2, var3, var5);
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      super.method_2(var1, var3, var5);
      if (this.method_5(var1, var3) && var5 == 0) {
         this.field_3980.run();
      }
   }

   public boolean method_949() {
      return true;
   }
}
