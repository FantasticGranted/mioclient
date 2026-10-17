package me.mioclient.internal;

import me.mioclient.internal.RenderUtil;
import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import me.mioclient.enum_.Class_0417;
import me.mioclient.module.render.ViewModelModule;
import me.mioclient.record.ViewModelTransform;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;

public class Class_0217 extends ClickGui {
   public static final Color field_603 = new Color(255, 255, 255, 100);
   public final ViewModelModule viewmodel;
   public final LinkedList<ViewModelTransform> field_604 = new LinkedList<>();
   public final Timer field_605 = new Timer();
   public Class_0417 field_606;
   public ViewModelTransform field_607;
   public double field_608;
   public double field_609;
   public boolean field_610;

   public Class_0217(ViewModelModule var1) {
      super();
      this.viewmodel = var1;
      this.field_607 = ViewModelTransform.method_2(var1);
      this.field_604.add(this.field_607);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      ViewModelTransform var5 = this.field_607;
      if (this.method_252() != Class_0417.NONE) {
         float var6 = (float)MathUtil.method_2((this.field_608 - (double)var2) * 0.004999999888241291, 2)
            * (float)(this.field_610 ? -1 : 1);
         float var7 = (float)MathUtil.method_2((this.field_609 - (double)var3) * 0.004999999888241291, 2);
         if (KeybindUtil.method_24()) {
            if (Math.abs(var6) > Math.abs(var7)) {
               var7 = 0.0F;
            } else {
               var6 = 0.0F;
            }
         }

         if (this.method_252() == Class_0417.TRANSLATE) {
            if (KeybindUtil.method_23()) {
               if (this.field_610) {
                  this.viewmodel.offX.method_78(var5.getOffX() + var6);
                  this.viewmodel.offY.method_78(var5.getOffY() + var7);
               } else {
                  this.viewmodel.mainX.method_78(var5.getMainX() + var6);
                  this.viewmodel.mainY.method_78(var5.getMainY() + var7);
               }
            } else {
               this.viewmodel.offX.method_78(var5.getOffX() + var6);
               this.viewmodel.offY.method_78(var5.getOffY() + var7);
               this.viewmodel.mainX.method_78(var5.getMainX() + var6);
               this.viewmodel.mainY.method_78(var5.getMainY() + var7);
            }
         } else if (this.method_252() == Class_0417.SCALE) {
            if (KeybindUtil.method_24()) {
               var6 = Math.abs(Math.max(var6, var7)) * Math.signum(var6);
               var7 = Math.abs(Math.max(var6, var7)) * Math.signum(var7);
            }

            var6 = -var6;
            if (KeybindUtil.method_23()) {
               if (this.field_610) {
                  this.viewmodel.offScaleX.method_78(var5.getOffScaleX() + var6);
                  this.viewmodel.offScaleY.method_78(var5.getOffScaleY() + var7);
               } else {
                  this.viewmodel.mainScaleX.method_78(var5.getMainScaleX() + var6);
                  this.viewmodel.mainScaleY.method_78(var5.getMainScaleY() + var7);
               }
            } else {
               this.viewmodel.offScaleX.method_78(var5.getOffScaleX() + var6);
               this.viewmodel.offScaleY.method_78(var5.getOffScaleY() + var7);
               this.viewmodel.mainScaleX.method_78(var5.getMainScaleX() + var6);
               this.viewmodel.mainScaleY.method_78(var5.getMainScaleY() + var7);
            }
         } else if (this.method_252() == Class_0417.ROTATE) {
            var6 /= 0.005F;
            var7 /= 0.005F;
            if (KeybindUtil.method_23()) {
               if (this.field_610) {
                  this.viewmodel.offRotateX.method_78(MathUtil.method_4(var5.getOffRotateX() + var7));
                  this.viewmodel.offRotateY.method_78(MathUtil.method_4(var5.getOffRotateY() + var6));
               } else {
                  this.viewmodel.mainRotateX.method_78(MathUtil.method_4(var5.getMainRotateX() + var7));
                  this.viewmodel.mainRotateY.method_78(MathUtil.method_4(var5.getMainRotateY() + var6));
               }
            } else {
               this.viewmodel.offRotateX.method_78(MathUtil.method_4(var5.getOffRotateX() + var7));
               this.viewmodel.offRotateY.method_78(MathUtil.method_4(var5.getOffRotateY() + var6));
               this.viewmodel.mainRotateX.method_78(MathUtil.method_4(var5.getMainRotateX() + var7));
               this.viewmodel.mainRotateY.method_78(MathUtil.method_4(var5.getMainRotateY() + var6));
            }
         }
      }
   }

   @Override
   public void method_5(double var1, double var3, int var5) {
      this.method_2(this.method_253(var5));
      if (this.method_252() != Class_0417.NONE) {
         this.field_607 = ViewModelTransform.method_2(this.viewmodel);
         this.field_604.add(this.field_607);
         this.field_610 = var1 * UiScaleHook.method_78(this.field_1096)
            < (double)((float)mc.getWindow().getGuiScaledWidth() / 2F);
         this.field_608 = var1;
         this.field_609 = var3;
      }
   }

   @Override
   public void method_7(double var1, double var3, int var5) {
      this.method_2(Class_0417.NONE);
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      if (verticalAmount > -1.0 && verticalAmount < 1.0) {
         return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
      } else {
         boolean var9 = mouseX * UiScaleHook.method_78(this.field_1096)
            < (double)((float)mc.getWindow().getGuiScaledWidth() / 2F);
         float var10 = (float)(verticalAmount * -0.10000000149011612);
         if (this.field_605.method_9(100L)) {
            this.field_604.add(ViewModelTransform.method_2(this.viewmodel));
         }

         this.field_605.reset();
         if (KeybindUtil.method_24()) {
            float var11 = MathUtil.method_4(this.viewmodel.offRotateZ.getValue() + (float)verticalAmount * 1e+01F);
            float var12 = MathUtil.method_4(this.viewmodel.mainRotateZ.getValue() + (float)verticalAmount * 1e+01F);
            if (KeybindUtil.method_23()) {
               if (var9) {
                  this.viewmodel.offRotateZ.method_78(var11);
               } else {
                  this.viewmodel.mainRotateZ.method_78(var12);
               }
            } else {
               this.viewmodel.offRotateZ.method_78(var11);
               this.viewmodel.mainRotateZ.method_78(var12);
            }

            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
         } else if (KeybindUtil.method_25()) {
            this.method_2(this.viewmodel.mainScaleZ, this.viewmodel.offScaleZ, var9, var10);
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
         } else {
            this.method_2(this.viewmodel.mainZ, this.viewmodel.offZ, var9, var10);
            return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
         }
      }
   }

   @Override
   public boolean keyPressed(KeyEvent event) {
      int keyCode = event.key();
      int scanCode = event.scancode();
      int modifiers = event.modifiers();
      if (KeybindUtil.method_23() && keyCode == 90 && !this.field_604.isEmpty()) {
         this.field_604.getLast().method_9(this.viewmodel);
         if (this.field_604.size() != 1) {
            this.field_604.removeLast();
         }
      }

      if (keyCode == 261) {
         this.field_607 = ViewModelTransform.identity();
         this.field_607.method_9(this.viewmodel);
         this.field_604.add(this.field_607);
      }

      return super.keyPressed(event);
   }

   @Override
   public void method_38(GuiGraphicsExtractor var1) {
      float var2 = (float)var1.guiWidth();
      float var3 = (float)var1.guiHeight();
      RenderUtil.field_2672
         .method_9(
            RenderUtil.mioPose(var1.pose()),
            var2 / 2F - 1F,
            0.0F,
            var2 / 2F + 1F,
            48F,
            field_603
         );
      List<String> var4 = List.of(
         "Left Click - Rotate",
         "Right Click - Move",
         "Middle Click - Scale",
         "Mouse Scroll - Move by Z",
         "Mouse Scroll + Shift - Rotate by Z",
         "Mouse Scroll + Alt - Scale by Z",
         "Hold Ctrl - Modify one",
         "Hold Shift - Lock axis",
         "Ctrl + Z - Undo",
         "Delete - Reset to defaults"
      );
      int var5 = 2;

      for (String var7 : var4) {
         float var8 = FontRenderer.field_3143.method_221(var7);
         FontRenderer.field_3143
            .method_9(var1, var7, (var2 - var8) / 2F, (float)(50 + var5), Colors.method_9(Color.white, 180));
         var5 += FontRenderer.field_3143.method_66() + 1;
      }

      RenderUtil.field_2672
         .method_9(
            RenderUtil.mioPose(var1.pose()),
            var2 / 2F - 1F,
            (float)(var5 + 52),
            var2 / 2F + 1F,
            var3,
            field_603
         );
   }

   public Class_0417 method_252() {
      return this.field_606;
   }

   public Class_0417 method_253(int var1) {
      return switch (var1) {
         case 0 -> Class_0417.ROTATE;
         case 1 -> Class_0417.TRANSLATE;
         case 2 -> Class_0417.SCALE;
         default -> Class_0417.NONE;
      };
   }

   public void method_2(Setting<Float> var1, Setting<Float> var2, boolean var3, float var4) {
      if (KeybindUtil.method_23()) {
         if (var3) {
            var2.method_78((Float)var2.getValue() + var4);
         } else {
            var1.method_78((Float)var1.getValue() + var4);
         }
      } else {
         var1.method_78((Float)var1.getValue() + var4);
         var2.method_78((Float)var2.getValue() + var4);
      }
   }

   public void method_2(Class_0417 var1) {
      this.field_606 = var1;
   }
}
