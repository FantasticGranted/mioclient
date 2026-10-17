package me.mioclient.module.abstract_;

import me.mioclient.internal.ResizableHudElement;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ChatComponent;

public class AbstractModule_14 extends AbstractModule_26 {
   public AbstractModule_14() {
      super("Chat");
      ResizableHudElement var1 = new ResizableHudElement(this);
      this.method_2(var1);
   }

   public float method_647() {
      if (this.field_1844.method_193() == null) {
         return 0.0F;
      } else {
         float var1 = (float)mc.getWindow().getGuiScaledHeight();
         float var2 = this.method_174();
         float var3 = var1 - 4e+01F;
         return (this.field_1844.method_60() - var3) / var2 + this.method_575();
      }
   }

   public float method_648() {
      if (this.field_1844.method_193() == null) {
         return 0.0F;
      } else {
         float var1 = this.method_174();
         return this.field_1844.method_59() / var1;
      }
   }

   public float method_174() {
      return ((Double)mc.options.chatScale().get()).floatValue();
   }

   public float method_575() {
      return (float)ChatComponent.getHeight((double)((Double)mc.options.chatHeightUnfocused().get()).floatValue());
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      super.method_2(var1);
   }

   @Override
   public float[] method_31() {
      return new float[]{
         ((Double)mc.options.chatWidth().get()).floatValue() * 2.8e+02F
            + 4e+01F
            + 12F * this.method_174(),
         this.method_575() * this.method_174()
      };
   }
}
