package me.mioclient.internal;

import com.mojang.blaze3d.vertex.PoseStack;
import me.mioclient.Hub;
import me.mioclient.api.CloseableState;
import me.mioclient.setting.Setting;

/** Base ClickGui component bound to a {@code Setting<T>}: handles visibility, animation and layout. */
public abstract class SettingWidget<T> extends PanelComponent {
   public final CloseableState field_3137;
   public final Setting<T> field_3138;
   public final TimedAnimation field_3139 = new TimedAnimation();
   public boolean field_3140;
   public long field_3141;
   public boolean field_3142;

   public SettingWidget(ClickGuiPanel var1, Setting<T> var2) {
      this(var1, () -> false, var2);
   }

   public SettingWidget(ClickGuiPanel var1, CloseableState var2, Setting<T> var3) {
      super(var1, 0);
      this.field_3137 = var2;
      this.field_3138 = var3;
   }

   @Override
   public int method_66() {
      return !this.field_3138.method_176() ? 0 : this.method_851();
   }

   public CloseableState method_909() {
      return this.field_3137;
   }

   public Setting<T> method_910() {
      return this.field_3138;
   }

   public boolean method_911() {
      boolean var1 = !this.field_3138.method_176() || this.field_3137.isClosed();
      if (var1) {
         this.field_3138.field_111 = false;
      }

      return var1;
   }

   public void method_2(PoseStack var1, String var2, Runnable var3) {
      var1.pushPose();
      this.method_468(FontRenderer.field_3143.method_221(var2));
      float var4 = -this.field_3139.method_45();
      boolean var5 = var4 != 0.0F && !(this instanceof EnumSettingWidget);
      if (var5) {
         ModulePanel var6 = (ModulePanel)this.field_3137;
         Render2D.method_474();
         ScissorStack.method_9(
            this.field_418.getX() + this.method_170(),
            this.field_418.getY() + var6.method_168(),
            this.field_418.method_216() - this.method_170(),
            var6.method_66()
         );
      }

      var1.translate(var4, 0.0F, 0.0F);
      var3.run();
      if (var5) {
         Render2D.method_474();
         ScissorStack.method_57();
      }

      var1.popPose();
   }

   public void method_468(float var1) {
      if (var1 <= (float)(this.field_418.method_216() - 4)) {
         this.field_3139.method_36(0.0F);
      } else {
         if (this.field_421) {
            if (!this.field_3140) {
               this.field_3141 = System.currentTimeMillis();
            }

            if (System.currentTimeMillis() > this.field_3141 + 750L) {
               this.field_3139.method_2(var1 - (float)(this.field_418.method_216() - 4), 500L);
            }
         } else {
            this.field_3139.method_2(0.0F, 500L);
         }

         this.field_3140 = this.field_421;
      }
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (!this.method_911()) {
         super.method_2(var1, var3, var5);
      }
   }

   public Class_0406 method_912() {
      return Hub.method_755();
   }
}
