package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Class_0458 extends LabelComponent {
   public final SoundId field_1463;
   public final SoundSelectGui field_1464;

   public Class_0458(SoundSelectGui var1, ClickGuiPanel var2, SoundId var3) {
      super(var2, var3.getName());
      this.field_1464 = var1;
      this.field_1463 = var3;
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.field_1464.field_2897.getValue().equals(this.field_1463)) {
         RenderUtil.field_2672
            .method_9(
               var2,
               (float)(this.field_418.getX() + 1),
               (float)(this.field_418.getY() + this.field_419) + 0.5F,
               (float)(this.field_418.getX() + this.field_418.method_216() - 1),
               (float)(this.field_418.getY() + this.field_419 + this.method_66()) - 0.5F,
               this.method_852().color.getValue()
            );
      } else {
         RenderUtil.field_2672
            .method_9(
               var2,
               (float)(this.field_418.getX() + 1),
               (float)(this.field_418.getY() + this.field_419) + 0.5F,
               (float)(this.field_418.getX() + this.field_418.method_216() - 1),
               (float)(this.field_418.getY() + this.field_419 + this.method_66()) - 0.5F,
               this.method_852().bgButton.getValue()
            );
      }

      super.method_2(var1, var2, var3, var5);
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (this.field_421) {
         if (var5 == 0) {
            this.field_1464.field_2897.method_78(this.field_1463);
            this.field_1464.method_257();
         } else if (var5 == 1) {
            Hub.field_2606.method_2(this.field_1463, 1F);
         }
      }
   }
}
