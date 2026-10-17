package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.CloseableState;
import me.mioclient.enum_.CursorShape;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;
import org.lwjgl.glfw.GLFW;

/** ClickGui widget for a String Setting: an editable text field. */
public class StringSettingWidget extends SettingWidget<String> {
   public final Timer field_2779 = new Timer();
   public boolean field_1309 = false;
   public String field_2780;
   public int field_2781 = 0;

   @SuppressWarnings({"unchecked", "rawtypes"})
   public StringSettingWidget(ClickGuiPanel var1, CloseableState var2, Setting<?> var3) {
      super(var1, var2, (Setting<String>)(Setting)var3);
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (this.method_911()) {
         this.field_3142 = false;
      } else {
         super.method_2(var1, var3, var5);
         if (this.method_5(var1, var3) && var5 == 0) {
            this.field_3142 = !this.field_3142;
            if (this.field_3142) {
               if (this.method_912().method_454() != null && this.method_912().method_454() != this) {
                  this.method_912().method_454().field_3142 = false;
               }

               this.method_912().method_2(this);
            } else if (this.method_912().method_454() == this) {
               this.method_912().method_2(null);
            }

            this.field_2780 = this.field_3138.getValue();
            if (this.field_2780 != null && !this.field_2780.isEmpty()) {
               this.field_2781 = this.field_2780.length();
            }
         }
      }
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      if (this.method_911()) {
         if (this.field_3142 && this.method_912().method_454() == this) {
            this.method_912().method_2(null);
         }

         this.field_3142 = false;
      } else {
         super.method_2(var1, var2, var3, var5);
         if (this.field_421) {
            ClickGui.field_3460 = CursorShape.INPUT;
         }

         Render2D.method_4(
            var2,
            (float)(this.field_418.getX() + this.method_170()),
            (float)(this.field_418.getY() + this.field_419) + 0.5F,
            (float)(this.field_418.getX() + this.field_418.method_216() - 1),
            (float)(this.field_418.getY() + this.field_419 + this.method_66()) - 0.5F,
            this.method_852().color.getValue()
         );
         if (this.field_2780 != null) {
            this.field_2781 = Mth.clamp(this.field_2781, 0, this.field_2780.length());
         }

         if (this.field_3142) {
            var2.pushPose();
            String var7 = this.method_453();
            float var8 = FontRenderer.field_3143.method_221(new PlaceholderText().method_2(this.field_2780.substring(0, this.field_2781)).method_9("\u0001_"));
            if (var8 > (float)(this.method_167().method_216() - this.method_170() * 2)) {
               var2.translate((float)(this.method_167().method_216() - this.method_170() * 2) - var8, 0.0F, 0.0F);
            }

            if (!this.field_2780.isEmpty()) {
               FontRenderer.field_3143
                  .method_9(
                     var1,
                     this.field_2780,
                     (float)(this.field_418.getX() + 4),
                     (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                     this.method_852().textColor.getValue()
                  );
            }

            if (!var7.isBlank()) {
               FontRenderer.field_3143
                  .method_9(
                     var1,
                     var7,
                     (float)(this.field_418.getX() + 4) + FontRenderer.field_3143.method_221(this.field_2780.substring(0, this.field_2781)),
                     (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                     this.method_852().textColor.getValue()
                  );
            }

            var2.popPose();
         } else {
            String var9 = new PlaceholderText().method_2(this.field_3138.getValue()).method_2(this.field_3138.getName()).method_9("\u0001: \u0001");
            this.method_2(
               var2,
               var9,
               () -> FontRenderer.field_3143
                     .method_9(
                        var1,
                        new PlaceholderText().method_2(this.field_3138.getValue()).method_2(this.field_3138.getName()).method_9("\u0001: \u0001"),
                        (float)(this.field_418.getX() + 4),
                        (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                        this.method_852().textColor.getValue()
                     )
            );
         }
      }
   }

   @Override
   public void method_68(int var1) {
      if (!this.method_911()) {
         if (this.field_3142) {
            Hub.method_755().method_1003();
            switch (var1) {
               case 86:
                  if (GLFW.glfwGetKey(mc.getWindow().handle(), 341) == 1) {
                     this.field_2780 = new StringBuilder(this.field_2780).insert(this.field_2781, mc.keyboardHandler.getClipboard()).toString();
                  }
                  break;
               case 256:
                  this.field_3142 = false;
                  break;
               case 257:
                  this.field_3142 = false;
                  this.field_3138.method_78(this.field_2780);
                  this.field_2781 = this.field_2780.length();
                  break;
               case 259:
                  if (this.field_2780.length() == 0 || this.field_2781 == 0) {
                     return;
                  }

                  int var2 = this.field_2781 - 1;
                  if (this.field_2781 == this.field_2780.length()) {
                     this.field_2780 = this.field_2780.substring(0, this.field_2780.length() - 1);
                  } else if (var2 >= 0 && var2 <= this.field_2780.length()) {
                     this.field_2780 = new StringBuilder(this.field_2780).deleteCharAt(var2).toString();
                  }

                  this.field_2781--;
                  break;
               case 262:
                  if (this.field_2781 < this.field_2780.length()) {
                     this.field_2781++;
                  }
                  break;
               case 263:
                  if (this.field_2781 > 0) {
                     this.field_2781--;
                  }
            }
         }
      }
   }

   @Override
   public void method_9(char var1) {
      if (!this.method_911()) {
         if (this.field_3142) {
            this.field_2780 = new StringBuilder(this.field_2780).insert(this.field_2781, var1).toString();
            this.field_2781++;
         }
      }
   }

   public String method_453() {
      if (this.field_2779.method_9(500L)) {
         this.field_1309 = !this.field_1309;
         this.field_2779.reset();
      }

      return this.field_1309 ? "_" : "";
   }
}
