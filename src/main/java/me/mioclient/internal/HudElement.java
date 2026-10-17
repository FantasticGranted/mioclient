package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.module.abstract_.AbstractModule_26;
import me.mioclient.module.client.FontsModule;
import me.mioclient.record.Rect;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.util.Mth;

/** Base positionable HUD element wrapping a HUD module: screen-corner anchoring, drag box and hover highlight. */
public abstract class HudElement implements MinecraftAccess, GuiComponent {
   public static final Color field_459 = new Color(0, 0, 0, 120);
   public static boolean field_460 = false;
   public final AbstractModule_26 field_461;
   public final TimedAnimation field_462 = new TimedAnimation();
   public final TimedAnimation field_463 = new TimedAnimation();
   public ScreenCorner field_464 = ScreenCorner.NONE;
   public float field_465 = 0.5F;
   public float field_466 = 0.5F;
   public int field_467;
   public int field_468;
   public float[] field_469;
   public boolean field_470;
   public long field_471;

   public HudElement(AbstractModule_26 var1) {
      super();
      this.field_461 = var1;
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      float[] var7 = this.method_193();
      if (this.method_5(var3, var5) && !this.field_470) {
         if (this.field_471 == -1L) {
            this.field_471 = System.currentTimeMillis();
         }

         if (this.field_471 + 300L <= System.currentTimeMillis()) {
            float var8 = this.method_195() == 1
               ? this.method_59() + var7[0] + 2F
               : this.method_59() - 2F - FontRenderer.field_3143.method_221(this.field_461.getName());
            Class_0617.field_1951.method_2(this.field_461.getName(), var8, this.method_60(), this.method_195());
         }
      } else {
         this.field_471 = -1L;
      }

      if (!field_460) {
         float var13 = this.method_59();
         float var9 = this.method_60();
         float var10 = 1.0F;
         float var11 = Hub.method_756().method_937() * var10 * (float)field_459.getAlpha() / 255F;
         Render2D.method_9(
            var2,
            var13,
            var9,
            var13 + Math.max(var7[0], 4F),
            var9 + Math.max(var7[1], 4F),
            Colors.method_9(field_459, var11)
         );
         float var12 = this.field_462.method_45();
         if (var12 > 0.0F) {
            Render2D.method_29(
               var2,
               var13 - 1F,
               var9 - 1F,
               var13 + Math.max(var7[0], 4F),
               var9 + Math.max(var7[1], 4F),
               Colors.method_9(Colors.method_959(), (int)(var12 * 255F))
            );
         }
      }
   }

   @Override
   public boolean method_5(double var1, double var3) {
      float[] var5 = this.method_193();
      return var1 >= (double)this.method_59()
         && var1 <= (double)(this.method_59() + Math.max(var5[0], 4F))
         && var3 >= (double)this.method_60()
         && var3 <= (double)(this.method_60() + Math.max(var5[1], 4F));
   }

   public void method_189() {
      this.field_463.method_2(this.method_176() ? 1F : 0.0F, 250L);
   }

   public abstract float[] method_31();

   public float method_59() {
      return Math.min(this.field_465 * (float)mc.getWindow().getGuiScaledWidth(), (float)mc.getWindow().getGuiScaledWidth() - this.method_193()[0]);
   }

   public float method_190() {
      return this.field_465;
   }

   public void method_2(float var1, boolean var2) {
      if (var2) {
         this.field_465 = var1 / (float)mc.getWindow().getGuiScaledWidth();
      } else {
         this.field_465 = Mth.clamp(var1, 0.0F, 1F);
      }
   }

   public float method_60() {
      return Math.min(this.field_466 * (float)mc.getWindow().getGuiScaledHeight(), (float)mc.getWindow().getGuiScaledHeight() - this.method_193()[1]);
   }

   public float method_191() {
      return this.field_466;
   }

   public void method_9(float var1, boolean var2) {
      if (var2) {
         this.field_466 = var1 / (float)mc.getWindow().getGuiScaledHeight();
      } else {
         this.field_466 = Mth.clamp(var1, 0.0F, 1F);
      }
   }

   @Override
   public int method_66() {
      int var1 = FontRenderer.field_3143.method_66();
      if (FontsModule.field_364.isToggled()) {
         var1--;
      }

      return var1;
   }

   public void method_6(boolean var1) {
      if (this.field_470 != var1) {
         this.field_462.method_2(var1 ? 1F : 0.0F, 250L);
         this.field_470 = var1;
      }
   }

   public boolean method_192() {
      return this.field_470;
   }

   public float[] method_193() {
      return this.field_469;
   }

   public void method_39(float[] var1) {
      this.field_469 = var1;
   }

   public ScreenCorner method_3() {
      return this.field_464;
   }

   public void method_2(ScreenCorner var1) {
      this.field_464 = var1;
   }

   public boolean method_176() {
      return this.field_461.isToggled();
   }

   public float method_176(float var1) {
      if (this.field_464 == ScreenCorner.TOP_CENTER) {
         return this.method_59() + this.field_469[0] / 2F - var1 / 2F;
      } else {
         return this.method_195() == -1 ? this.method_59() + this.field_469[0] - var1 : this.method_59();
      }
   }

   public float method_194(float var1) {
      return this.method_196() == -1 ? this.method_60() + this.field_469[1] - var1 : this.method_60();
   }

   public int method_195() {
      if (this.field_467 == 0) {
         this.method_197();
      }

      return this.field_467;
   }

   public int method_196() {
      if (this.field_468 == 0) {
         this.method_197();
      }

      return this.field_468;
   }

   public void method_197() {
      this.field_467 = this.method_59() + this.field_469[0] / 2F
            > (float)mc.getWindow().getGuiScaledWidth() / 2F
         ? -1
         : 1;
      this.field_468 = this.method_60() + this.field_469[1] / 2F
            > (float)mc.getWindow().getGuiScaledHeight() / 2F
         ? -1
         : 1;
   }

   public Rect method_198() {
      return Rect.method_9(
         this.field_465 * (float)mc.getWindow().getGuiScaledWidth(),
         this.field_466 * (float)mc.getWindow().getGuiScaledHeight(),
         this.field_469[0],
         this.field_469[1]
      );
   }

   public AbstractModule_26 method_199() {
      return this.field_461;
   }

   public float method_200() {
      return this.field_463.method_45();
   }
}
