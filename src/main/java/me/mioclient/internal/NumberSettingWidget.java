package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import me.mioclient.Hub;
import me.mioclient.api.CloseableState;
import me.mioclient.enum_.CursorShape;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

/** ClickGui slider widget for a numeric Setting. */
public class NumberSettingWidget extends SettingWidget<Number> {
   public static final NumberFormat field_3627 = NumberFormat.getNumberInstance();
   public final AnimatedValue field_3628 = new AnimatedValue(6F, false);
   public final Timer field_3629 = new Timer();
   public int field_1321;
   public int field_1322;
   public boolean field_470;
   public boolean field_1309 = false;
   public float field_3630;
   public String field_2780 = "";

   @SuppressWarnings({"unchecked", "rawtypes"})
   public NumberSettingWidget(ClickGuiPanel var1, CloseableState var2, Setting<?> var3) {
      super(var1, var2, (Setting<Number>)(Setting)var3);
   }

   public static double method_39(double var0) {
      BigDecimal var2 = new BigDecimal(var0);
      var2 = var2.setScale(4, RoundingMode.HALF_UP);
      return var2.doubleValue();
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (this.method_911()) {
         this.field_3142 = false;
      } else {
         super.method_2(var1, var3, var5);
         if (this.method_5(var1, var3) && var5 == 0 && this.field_3137.method_194()) {
            this.field_470 = true;
         }

         if (this.method_5(var1, var3) && var5 == 1 && this.field_3137.method_194()) {
            this.field_3142 = !this.field_3142;
            if (this.field_3142) {
               if (this.method_912().method_454() != null && this.method_912().method_454() != this) {
                  this.method_912().method_454().field_3142 = false;
               }

               this.method_912().method_2(this);
               this.field_2780 = this.field_3138.getValue().toString();
            } else if (this.method_912().method_454() == this) {
               this.method_912().method_2(null);
            }
         }
      }
   }

   @Override
   public void method_9(double var1, double var3, int var5) {
      this.field_470 = false;
   }

   @Override
   public void method_68(int var1) {
      if (!this.method_911()) {
         if (this.field_3142 && this.field_3137.method_194()) {
            switch (var1) {
               case 256:
                  this.field_3142 = false;
                  Hub.method_755().method_1003();
                  break;
               case 257:
               case 335:
                  this.field_3142 = false;
                  boolean var2 = this.field_2780 == null || this.field_2780.isBlank();

                  try {
                     String var3 = this.field_3138.getValue().getClass().getSimpleName();
                     switch (var3) {
                        case "Integer":
                           this.field_3138.method_78((Number)(var2 ? this.field_3138.getValue() : Math.round(Double.parseDouble(this.field_2780))));
                           return;
                        case "Float":
                           Setting var6 = this.field_3138;
                           Object var8;
                           if (var2) {
                              var8 = this.field_3138.getValue();
                           } else {
                              var8 = this.field_2780;
                              var8 = Float.parseFloat(new PlaceholderText().method_2(var8).method_9("\u0001f"));
                           }

                           var6.method_78(var8);
                           return;
                        case "Double":
                           Setting var10000 = this.field_3138;
                           Object var10001;
                           if (var2) {
                              var10001 = this.field_3138.getValue();
                           } else {
                              var10001 = this.field_2780;
                              var10001 = Double.parseDouble(new PlaceholderText().method_2(var10001).method_9("\u0001d"));
                           }

                           var10000.method_78(var10001);
                     }
                  } catch (Exception var5) {
                  }
                  break;
               case 259:
                  if (this.field_2780.length() > 0) {
                     this.field_2780 = this.field_2780.substring(0, this.field_2780.length() - 1);
                  }
            }
         }
      }
   }

   @Override
   public void method_9(char var1) {
      if (!this.method_911()) {
         String var2 = "1234567890.-";
         if (this.field_3142 && this.field_3137.method_194() && FontRenderer.method_2(var2, var1)) {
            if (var1 == '.' && FontRenderer.method_2(this.field_2780, var1)) {
               return;
            }

            String var10001 = this.field_2780;
            this.field_2780 = new PlaceholderText().method_2(var1).method_2((Object)var10001).method_9("\u0001\u0001");
         }
      }
   }

   @Override
   public void method_4(double var1, double var3) {
      if (this.method_911()) {
         this.field_3628.method_36(0.0F);
      }

      this.field_1322 = this.field_418.getY() + this.field_419;
      this.field_1321 = this.field_418.getX();
      double var5 = (double)(this.method_167().method_216() - 2);
      double var7 = Math.min(var5, Math.max(0.0, var1 - (double)this.field_1321));
      double var9 = this.field_3138.method_100().doubleValue();
      double var11 = this.field_3138.method_101().doubleValue();
      double var13 = this.field_3138.getValue().getClass().getSimpleName().equalsIgnoreCase("Integer")
         ? 1.0
         : 0.1;
      this.field_3630 = (float)(
         var5
            * (
               (double)(
                     var13 == 1.0
                        ? (float)this.field_3138.getValue().intValue()
                        : this.field_3138.getValue().floatValue()
                  )
                  - var9
            )
            / (var11 - var9)
      );
      if (this.field_470) {
         if (var7 == 0.0) {
            if (var13 == 1.0) {
               this.field_3138.method_78((int)this.field_3138.method_100().floatValue());
            } else {
               this.field_3138.method_78(this.field_3138.method_100().floatValue());
            }
         } else {
            float var15 = (float)(var7 / var5 * (var11 - var9) + var9);
            float var16 = (float)(1.0 / var13);
            double var17 = Math.max(var9, Math.min(var11, (double)var15));
            if (var13 == 0.1) {
               this.field_3138.method_78(method_39((double)((float)Math.round(var17 * (double)var16) / var16)));
            } else {
               this.field_3138.method_78(method_39(var17));
            }
         }
      }

      if (this.field_3138.getValue().floatValue() >= this.field_3138.method_101().floatValue()) {
         this.field_3630 = (float)(this.method_167().method_216() - 2);
      }

      this.field_3628.method_3(this.field_3630);
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
         if (this.field_421 && this.field_3142) {
            ClickGui.field_3460 = CursorShape.INPUT;
         }

         Render2D.method_4(
            var2,
            (float)(this.field_1321 + this.method_170()),
            (float)this.field_1322 + 0.5F,
            (float)(
               (int)(
                  (float)this.field_1321
                     + Mth.clamp(this.field_3628.method_45(), 0.0F, (float)(this.field_418.method_216() - 3))
                     + 2F
               )
            ),
            (float)(this.field_1322 + this.method_66()) - 0.5F,
            this.method_852().color.getValue()
         );
         String var7 = this.field_3142
            ? (this.field_2780 == null ? "" : this.field_2780)
            : (
               this.field_3138.method_468()
                  ? new PlaceholderText().method_2(this.field_3138.method_157()).method_2(this.field_3138.getName()).method_9("\u0001: \u0001")
                  : new PlaceholderText()
                     .method_2(((CustomSetting3)this.field_3138).method_323())
                     .method_2(
                        String.format("%s: %s", this.field_3138.getName(), field_3627.format(this.field_3138.getValue().doubleValue())).replace(",", ".")
                     )
                     .method_9("\u0001\u0001")
            );
         this.method_2(
            var2,
            this.method_664(var7),
            () -> FontRenderer.field_3143
                  .method_9(
                     var1,
                     new PlaceholderText().method_2(this.method_453()).method_2((Object)var7).method_9("\u0001\u0001"),
                     (float)(this.field_418.getX() + 4),
                     (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                     this.method_852().textColor.getValue()
                  )
         );
      }
   }

   @Override
   public boolean method_5(double var1, double var3) {
      return super.method_5(var1, var3) || this.field_470;
   }

   @Override
   public int method_66() {
      return !this.field_3138.method_176() ? 0 : this.method_851();
   }

   public String method_664(String var1) {
      String var10001 = this.field_3142 ? "_" : "";
      return new PlaceholderText().method_2((Object)var10001).method_2((Object)var1).method_9("\u0001\u0001");
   }

   public String method_453() {
      if (this.field_3629.method_9(500L)) {
         this.field_1309 = !this.field_1309;
         this.field_3629.reset();
      }

      return this.field_1309 && this.field_3142 ? "_" : "";
   }

   static {
      field_3627.setMinimumFractionDigits(1);
      field_3627.setMaximumFractionDigits(4);
      field_3627.setGroupingUsed(false);
   }
}
