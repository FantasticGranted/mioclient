package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.enum_.Class_1290;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.ColorSetting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class Class_0781 extends BooleanSettingWidget {
   public Class_0781(ColorSettingWidget var1) {
      super(var1.method_167(), var1, new BooleanSetting("", ((ColorSetting)var1.method_910()).method_132()));
      this.field_3138.method_9(() -> ((ColorSetting)var1.method_910()).method_4(this.field_3138.getValue()));
      this.method_2(new Class_1290[]{Class_1290.PADDING_SHIFT});
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      super.method_2(var1, var2, var3, var5);
      String var7 = "Rainbow";
      this.method_2(
         var2,
         var7,
         () -> FontRenderer.field_3143
               .method_9(
                  var1,
                  var7,
                  (float)this.field_418.getX()
                     + (float)this.method_167().method_216() * 0.5F
                     - FontRenderer.field_3143.method_221(var7) * 0.5F,
                  (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                  this.method_852().textColor.getValue()
               )
      );
   }
}
