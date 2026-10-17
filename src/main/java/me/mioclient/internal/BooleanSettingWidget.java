package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.api.CloseableState;
import me.mioclient.module.client.UIModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** ClickGui widget for a boolean Setting: a toggle with on/off animation. */
public class BooleanSettingWidget extends SettingWidget<Boolean> {
   public final ToggleAnimation field_3189 = new ToggleAnimation(() -> 2F * UIModule.field_2843.animSpeed.getValue(), true);

   @SuppressWarnings({"unchecked", "rawtypes"})
   public BooleanSettingWidget(ClickGuiPanel var1, CloseableState var2, Setting<?> var3) {
      super(var1, var2, (Setting<Boolean>)(Setting)var3);
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (!this.method_911()) {
         if (!this.field_3138.field_3120 || var5 != 0) {
            super.method_2(var1, var3, var5);
            if (this.method_5(var1, var3)) {
               if (var5 == 0) {
                  this.field_3138.method_78(!this.field_3138.getValue());
               }

               if (var5 == 1 && this.field_3138.field_3118) {
                  this.field_3138.field_111 = !this.field_3138.field_111;
               }
            }
         }
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.method_911()) {
         this.field_3189.reset();
      } else {
         this.field_3189.method_3(this.field_3138.getValue());
         float var7 = this.field_3189.method_45();
         super.method_2(var1, var2, var3, var5);
         if ((this.field_3138.getValue() || (double)var7 > 0.01) && !this.field_3138.field_3120) {
            Render2D.method_4(
               var2,
               (float)(this.field_418.getX() + this.method_170()),
               (float)(this.field_418.getY() + this.field_419) + 0.5F,
               (float)this.field_418.getX()
                  + Math.max((float)this.field_418.method_216() * var7 - 1F, 2F),
               (float)(this.field_418.getY() + this.field_419 + this.method_66()) - 0.5F,
               this.method_852().color.getValue()
            );
         }

         String var8 = this.field_3138.getName();
         if (!var8.isEmpty()) {
            this.method_2(
               var2,
               var8,
               () -> FontRenderer.field_3143
                     .method_9(
                        var1,
                        var8,
                        (float)(this.field_418.getX() + 4),
                        (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                        this.method_852().textColor.getValue()
                     )
            );
         }

         if (this.field_3138.field_3118 && !this.field_3138.field_3120) {
            String var9 = this.field_3138.field_111 ? "-" : "+";
            FontRenderer.field_3143
               .method_9(
                  var1,
                  var9,
                  (float)(this.field_418.getX() + this.field_418.method_216() - 4) - FontRenderer.field_3143.method_221(var9),
                  (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                  this.method_852().textColor.getValue()
               );
         }

         if (this.field_3138.field_3120) {
            String var10 = "...";
            FontRenderer.field_3143
               .method_9(
                  var1,
                  var10,
                  (float)(this.field_418.getX() + this.field_418.method_216() - 4) - FontRenderer.field_3143.method_221(var10),
                  (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                  this.method_852().textColor.getValue()
               );
         }
      }
   }
}
