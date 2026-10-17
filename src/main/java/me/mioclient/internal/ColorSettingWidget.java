package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.CloseableState;
import me.mioclient.enum_.CursorShape;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

/** ClickGui color-picker widget for a {@code Setting<Color>}: HSB sliders with open/close animation. */
public class ColorSettingWidget extends SettingWidget<Color> implements CloseableState {
   public final List<GuiComponent> field_1641 = new ArrayList<>(3);
   public final Timer field_1642 = new Timer();
   public final Timer field_1643 = new Timer();
   public final Timer field_1644 = new Timer();
   public boolean field_111;
   public boolean field_257;
   public boolean field_1645;
   public boolean field_1646;
   public boolean field_1647;
   public float[] field_1648 = new float[3];
   public final AnimatedValue field_1649 = new AnimatedValue(5F, true);
   public final AnimatedValue field_1650 = new AnimatedValue(5F, true);
   public final AnimatedValue field_1651 = new AnimatedValue(5F, true);
   public final AnimatedValue field_1652 = new AnimatedValue(5F, true);
   public final ToggleAnimation field_1653 = new ToggleAnimation(4F, true);

   public ColorSettingWidget(ClickGuiPanel var1, CloseableState var2, Setting<?> var3) {
      super(var1, var2, (Setting<Color>)(Setting)var3);
      int var4 = (int)(
         (double)(this.field_419 + this.method_851() + this.method_167().method_216())
            + 6.5
            + (double)this.method_560()
      );
      this.field_1641.add(new ColorCopyButton(var4, this));
      this.field_1641.add(new ColorPasteButton(var4, this));
      if (!((ColorSetting)var3).method_494()) {
         this.field_1641.add(new Class_1311(this));
      }

      this.field_1641.add(new Class_0781(this));
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      super.method_2(var1, var2, var3, var5);
      if (this.method_911() || !this.field_111) {
         this.field_1649.method_36(0.0F);
         this.field_1650.method_36(0.0F);
         this.field_1651.method_36(0.0F);
         this.field_1652.method_36(0.0F);
      }

      if (!this.method_911()) {
         if (this.field_421 && this.field_111) {
            ClickGui.field_3460 = CursorShape.STANDARD;
         }

         this.method_2(
            var2,
            this.field_3138.getName(),
            () -> FontRenderer.field_3143
                  .method_9(
                     var1,
                     this.field_3138.getName(),
                     (float)(this.field_418.getX() + 4),
                     (float)this.field_418.getY() + this.method_850() - this.method_169() + (float)this.field_419,
                     this.method_852().textColor.getValue()
                  )
         );
         Render2D.method_4(
            var2,
            this.method_59() + (float)this.field_418.method_216() - 12.5F,
            this.method_60() + 1F,
            this.method_59() + (float)this.field_418.method_216() - 3.5F,
            this.method_60() + 1e+01F,
            this.method_852().color.getValue()
         );
         Render2D.method_4(
            var2,
            this.method_59() + (float)this.field_418.method_216() - 12F,
            this.method_60() + 1.5F,
            this.method_59() + (float)this.field_418.method_216() - 4F,
            this.method_60() + 9.5F,
            Colors.method_9(this.field_3138.getValue(), 255)
         );
         if (this.field_111) {
            int var7 = this.field_3138.getValue().getAlpha();
            int var8 = this.method_851();
            if (this.field_1645) {
               ClickGui.field_3460 = CursorShape.POINTER;
               this.field_1648[1] = (float)Mth.clamp(
                  (var3 - (double)this.method_59() - 5.0) / (double)(this.method_167().method_216() - 8),
                  0.0,
                  1.0
               );
               this.field_1648[2] = 1F
                  - (float)Mth.clamp(
                     (var5 - (double)this.method_60() - (double)var8 - 3.0)
                        / (double)(this.method_167().method_216() - 8),
                     0.0,
                     1.0
                  );
            } else if (this.field_1646) {
               ClickGui.field_3460 = CursorShape.POINTER;
               float var9 = (float)Math.min(
                  Math.max((double)this.method_59(), var3 - 1.0),
                  (double)(this.method_59() + (float)(this.method_167().method_216() - 4))
               );
               this.field_1648[0] = Math.min((var9 - this.method_59()) / (float)(this.method_167().method_216() - 4), 1F);
            } else if (this.field_1647 && !this.field_3138.field_3119) {
               ClickGui.field_3460 = CursorShape.POINTER;
               float var21 = (float)Math.min(
                  Math.max((double)this.method_59(), var3 - 3.0),
                  (double)(this.method_59() + (float)(this.method_167().method_216() - 4))
               );
               var7 = (int)(
                  Math.min(
                        1F - (var21 - this.method_59()) / (float)(this.method_167().method_216() - 4),
                        1F
                     )
                     * 255F
               );
            }

            int var22 = Color.HSBtoRGB(this.field_1648[0], 1F, 1F);
            float var10 = (float)(var22 >> 16 & 0xFF) / 255F;
            float var11 = (float)(var22 >> 8 & 0xFF) / 255F;
            float var12 = (float)(var22 & 0xFF) / 255F;
            int var13 = this.field_1645 ? -1 : this.method_852().color.getValue().hashCode();
            Render2D.method_9(
               var2.last().pose(),
               this.method_59() + 2.5F,
               this.method_60() + (float)var8 + 0.5F,
               (float)(this.method_167().method_216() - 3),
               (float)(this.method_167().method_216() - 3),
               var13,
               var13,
               var13,
               var13
            );
            Render2D.method_9(
               var2.last().pose(),
               this.method_59() + 3F,
               this.method_60() + (float)var8 + 1F,
               (float)(this.method_167().method_216() - 4),
               (float)(this.method_167().method_216() - 4),
               -1,
               var22,
               -16777216,
               -16777216
            );
            Render2D.method_9(
               var2.last().pose(),
               this.method_59() + 2.5F,
               this.method_60() + (float)this.method_167().method_216() + (float)var8 - 0.5F,
               (float)(this.method_167().method_216() - 3),
               7F,
               this.field_1646 ? -1 : this.method_559().hashCode(),
               this.field_1646 ? -1 : this.method_559().hashCode()
            );
            Render2D.method_9(
               var2.last().pose(),
               this.method_59() + 3F,
               this.method_60() + (float)this.method_167().method_216() + (float)var8,
               (float)(this.method_167().method_216() - 4),
               6F,
               -65536,
               -256,
               -16711936,
               -16711681,
               -16776961,
               -65281,
               -65536
            );
            this.method_2(this.field_1651, this.field_1648[0] * (float)(this.method_167().method_216() - 2));
            float var14 = Mth.clamp(this.field_1651.method_45(), 3F, (float)(this.method_167().method_216() - 3));
            Render2D.method_9(
               var2,
               (float)((int)(this.method_59() + var14)) - 0.5F,
               (float)((int)(this.method_60() + (float)this.method_167().method_216() + (float)var8)) - 0.5F,
               (float)((int)(this.method_59() + var14 + 2F)) + 0.5F,
               (float)((int)(this.method_60() + (float)this.method_167().method_216() + (float)var8 + 6.5F)),
               this.field_1645 ? -1 : this.method_559().hashCode()
            );
            Render2D.method_9(
               var2,
               (float)((int)(this.method_59() + var14)),
               (float)((int)(this.method_60() + (float)this.method_167().method_216() + (float)var8)),
               (float)((int)(this.method_59() + var14 + 2F)),
               (float)((int)(this.method_60() + (float)this.method_167().method_216() + (float)var8 + 6.5F)),
               -1
            );
            if (!this.field_3138.field_3119) {
               Render2D.method_9(
                  var2.last().pose(),
                  this.method_59() + 2.5F,
                  (float)((int)this.method_60() + this.method_167().method_216() + var8) + 7.5F,
                  (float)(this.method_167().method_216() - 3),
                  7F,
                  this.field_1647 ? -1 : this.method_559().hashCode(),
                  this.field_1647 ? -1 : this.method_559().hashCode()
               );
               this.method_2(
                  var2,
                  (int)this.method_59() + 3,
                  (int)this.method_60() + this.method_167().method_216() + var8 + 8,
                  this.method_167().method_216() - 4,
                  6,
                  var10,
                  var11,
                  var12,
                  (float)var7 / 255F
               );
            }

            this.method_2(
               this.field_1649, Mth.clamp(this.field_1648[1], 0.0F, 1F) * (float)(this.method_167().method_216() - 8)
            );
            this.method_2(
               this.field_1650,
               (1F - Mth.clamp(this.field_1648[2], 0.0F, 1F))
                  * (float)(this.method_167().method_216() - 8)
            );
            float var15 = this.method_59() + 5F + this.field_1649.method_45();
            float var16 = this.method_60() + (float)var8 + 3F + this.field_1650.method_45();
            this.field_1653.method_3(this.field_1645);
            float var17 = 2F * 1F * this.field_1653.method_45();
            Color var18 = Color.getHSBColor(this.field_1648[0], this.field_1648[1], this.field_1648[2]);
            Render2D.method_4(
               var2,
               var15 - 1.5F - var17,
               var16 - 1.5F - var17,
               var15 + 1.5F + var17,
               var16 + 1.5F + var17,
               this.field_1645 ? Color.white : this.method_559()
            );
            Render2D.method_4(
               var2,
               var15 - 1F - var17,
               var16 - 1F - var17,
               var15 + 1F + var17,
               var16 + 1F + var17,
               this.field_1645 ? var18 : var18.darker()
            );
            this.field_1641.forEach(var6 -> var6.method_2(var1, var2, var3, var5));
            this.field_3138
               .method_78(
                  Colors.method_9(new Color(Color.HSBtoRGB(this.field_1648[0], this.field_1648[1], this.field_1648[2])), Mth.clamp(var7, 0, 255))
               );
            if (((ColorSetting)this.field_3138).method_132()) {
               this.field_1648[0] = Color.RGBtoHSB(
                  this.field_3138.getValue().getRed(), this.field_3138.getValue().getGreen(), this.field_3138.getValue().getBlue(), null
               )[0];
            }
         }

         int var19 = (int)((float)(this.field_419 + this.method_851() + this.method_167().method_216() + 6) + this.method_560());
         float var20 = 1F;

         for (GuiComponent var24 : this.field_1641) {
            var24.method_4(var3, var5);
            var24.method_26(var19);
            if (!(var24 instanceof ColorPickerControl)) {
               var19 += (int)((float)var24.method_66() + var20);
               var24.method_26(var19);
               var20 = 0.5F;
            }
         }
      }
   }

   public void method_2(PoseStack var1, int var2, int var3, int var4, int var5, float var6, float var7, float var8, float var9) {
      boolean var10 = true;
      int var11 = var5 / 2;

      for (int var12 = -var11; var12 < var4; var12 += var11) {
         if (!var10) {
            Render2D.method_9(var1, (float)(var2 + var12), (float)var3, (float)(var2 + var12 + var11), (float)(var3 + var5), -1);
            Render2D.method_9(var1, (float)(var2 + var12), (float)(var3 + var11), (float)(var2 + var12 + var11), (float)(var3 + var5), -7303024);
            if (var12 < var4 - var11) {
               int var13 = var2 + var12 + var11;
               int var14 = Math.min(var2 + var4, var2 + var12 + var11 * 2);
               Render2D.method_9(var1, (float)var13, (float)var3, (float)var14, (float)(var3 + var5), -7303024);
               Render2D.method_9(var1, (float)var13, (float)(var3 + var11), (float)var14, (float)(var3 + var5), -1);
            }
         }

         var10 = !var10;
      }

      Render2D.method_9(
         var1.last().pose(),
         (float)var2,
         (float)var3,
         (float)var4,
         (float)var5,
         new Color(var6, var7, var8, 1F).hashCode(),
         0
      );
      this.method_2(this.field_1652, (float)var4 - (float)(var4 - 2) * var9);
      float var15 = (float)var2 + this.field_1652.method_45() - 1F;
      Render2D.method_4(
         var1,
         var15 - 1.5F,
         (float)var3 - 0.5F,
         var15 + 1.5F,
         (float)(var3 + var5) + 0.5F,
         this.field_1647 ? Color.white : this.method_559()
      );
      Render2D.method_9(var1, var15 - 1F, (float)var3, var15 + 1F, (float)(var3 + var5), -1);
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (!this.method_911()) {
         int var6 = this.method_851();
         if (this.method_38(var1, var3) && var5 == 1) {
            this.field_111 = !this.field_111;
         }

         if (this.field_111) {
            this.field_1641.forEach(var5x -> var5x.method_2(var1, var3, var5));
         }

         if (this.field_111 && var5 == 0) {
            if (var1 > (double)(this.method_59() + 2F)
               && var1 < (double)(this.method_59() + (float)this.method_167().method_216() - 2F)
               && var3 > (double)(this.method_60() + (float)var6)
               && var3 < (double)(this.method_60() + (float)var6 + (float)this.method_167().method_216() - 2F)) {
               this.field_1645 = true;
            }

            if (var1 > (double)(this.method_59() + 2F)
               && var1 < (double)(this.method_59() + (float)this.method_167().method_216() - 2F)
               && var3 > (double)(this.method_60() + (float)var6 + (float)this.method_167().method_216())
               && var3 < (double)(this.method_60() + (float)var6 + (float)this.method_167().method_216() + 6F)) {
               this.field_1646 = true;
            }

            if (var1 > (double)(this.method_59() + 2F)
               && var1 < (double)(this.method_59() + (float)this.method_167().method_216() - 2F)
               && var3 > (double)(this.method_60() + (float)var6 + (float)this.method_167().method_216() + 8F)
               && var3 < (double)(this.method_60() + (float)var6 + (float)this.method_167().method_216() + 14F)
               && !this.field_3138.field_3119) {
               this.field_1647 = true;
            }

            super.method_2(var1, var3, var5);
         }
      }
   }

   @Override
   public void method_9(double var1, double var3, int var5) {
      if (!this.method_911()) {
         this.field_1641.forEach(var5x -> var5x.method_9(var1, var3, var5));
         super.method_9(var1, var3, var5);
         this.field_1645 = this.field_1646 = this.field_1647 = false;
      }
   }

   @Override
   public void method_9(char var1) {
      if (!this.method_911()) {
         this.field_1641.forEach(var1x -> var1x.method_9(var1));
         super.method_9(var1);
      }
   }

   @Override
   public void method_68(int var1) {
      if (!this.method_911()) {
         this.field_1641.forEach(var1x -> var1x.method_68(var1));
         super.method_68(var1);
      }
   }

   @Override
   public int method_66() {
      if (this.field_111 && !this.method_911()) {
         int var1 = (int)((float)this.method_167().method_216() + 7F + (float)(this.method_851() * 2) + this.method_560());
         if (this.field_1641.size() > 2) {
            var1 = (int)((float)var1 + (float)(this.method_851() * (this.field_1641.size() - 2)) + 0.5F);
         }

         return var1;
      } else {
         return super.method_66();
      }
   }

   @Override
   public void init() {
      this.method_31(this.field_3138.getValue());
      this.field_1641.forEach(GuiComponent::init);
   }

   @Override
   public boolean isClosed() {
      return this.field_3137.isClosed() || !this.field_111;
   }

   public boolean method_38(double var1, double var3) {
      return var1 > (double)this.field_418.getX()
         && var1 < (double)(this.field_418.getX() + this.field_418.method_216())
         && var3 > (double)(this.field_418.getY() + this.field_419)
         && var3 < (double)(this.field_418.getY() + this.method_851() + this.field_419);
   }

   public float method_59() {
      return (float)this.field_418.getX();
   }

   public float method_60() {
      return (float)(this.field_418.getY() + this.field_419);
   }

   public Color method_559() {
      return Colors.method_2(this.method_852().color.getValue(), 1F);
   }

   public float method_560() {
      return this.field_3138.field_3119 ? 2F : 1e+01F;
   }

   @Override
   public boolean method_5(double var1, double var3) {
      return super.method_5(var1, var3) || this.field_1646 || this.field_1645 || this.field_1647;
   }

   public void method_31(Color var1) {
      float[] var2 = Color.RGBtoHSB(var1.getRed(), var1.getGreen(), var1.getBlue(), null);
      this.field_1648[0] = var2[0];
      this.field_1648[1] = var2[1];
      this.field_1648[2] = var2[2];
   }

   public Color method_152() {
      return Colors.method_9(Color.getHSBColor(this.field_1648[0], this.field_1648[1], this.field_1648[2]), this.field_3138.getValue().getAlpha());
   }

   public void method_2(AnimatedValue var1, float var2) {
      if (this.field_257) {
         var1.method_36(var2);
      } else {
         var1.method_3(var2);
      }
   }
}
