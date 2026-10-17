package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.api.Toggleable;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public abstract class Class_0959 extends ColorPickerControl implements Toggleable {
   public boolean field_2971;

   public Class_0959(int var1, ColorSettingWidget var2, String var3, Runnable var4) {
      super(var1, var2, var3, var4);
      this.field_91 = () -> {
         var4.run();
         this.field_2971 = !this.field_2971;
      };
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (!this.field_89.method_911()) {
         FontRenderer.field_3143
            .method_9(
               var1,
               this.field_90,
               (float)this.field_418.getX() + this.method_59(),
               (float)(this.field_418.getY() + this.field_419) + this.method_60() + this.method_850() - this.method_169(),
               this.field_2971 ? this.method_852().color.getValue() : this.method_852().textColor.getValue()
            );
      }
   }

   @Override
   public boolean isToggled() {
      return this.field_2971;
   }

   @Override
   public void enable() {
   }

   @Override
   public void disable() {
   }
}
