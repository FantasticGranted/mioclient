package me.mioclient.internal;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.module.client.UIModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

public class Class_0325 {
   public static final String field_1094 = "❆";
   public static final float field_1095 = 0.033F;
   public final float field_1086 = (float)Mth.clamp(
      Math.random(), 0.07000000029802322, 0.18000000715255737
   );
   public final float field_1096 = MathUtil.method_2((float)Class_0245.field_688, 1.15F);
   public final double field_608 = Math.random();
   public final double field_1097 = (double)MathUtil.method_2(-0.033F, 0.033F);
   public float field_877 = MathUtil.method_2(-0.45F, 0.0F);

   public Class_0325() {
      super();
   }

   public void method_2(GuiGraphicsExtractor var1) {
      Font var2 = Minecraft.getInstance().font;
      double var3 = this.field_608 + (double)this.field_877 * this.field_1097;
      float var5 = (float)(var3 * (double)var1.guiWidth());
      float var6 = this.field_877 * (float)var1.guiHeight() * 0.5F
         - 9F
         - 1F;
      this.field_877 = this.field_877 + Hub.field_2601.method_154(0.5F) * this.field_1086;
      this.field_877 = Math.min(this.field_877, 1F);
      if (UIModule.field_2843.snow.getValue() && this.field_877 >= 0.0F) {
         int var7 = Mth.ceil(
            Math.max(255F - this.field_877 * 255F, 4F)
         );
         var1.pose().pushMatrix();
         var1.pose().scale(this.field_1096,  this.field_1096);
         var1.pose().translate(var5 / this.field_1096,  var6 / this.field_1096);
         var1.centeredText(var2, "❆", 0, 0, new Color(255, 255, 255, var7).hashCode());
         var1.pose().popMatrix();
      }
   }

   public double method_380() {
      return this.field_608;
   }

   public boolean method_381() {
      return this.field_877 >= 1F;
   }
}
