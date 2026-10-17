package me.mioclient.internal;

import me.mioclient.internal.RenderUtil;
import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.client.HUDModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public final class Class_0051 implements MinecraftAccess {
   public static HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   public boolean field_116;
   public boolean field_117;
   public boolean field_118;
   public boolean field_119;
   public final TimedAnimation field_120 = new TimedAnimation();
   public final TimedAnimation field_121 = new TimedAnimation();

   public Class_0051() {
      super();
   }

   public void method_9(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      int var5 = var1.guiWidth();
      int var6 = var1.guiHeight();
      boolean var7 = this.getId((double)var2) || this.field_118;
      boolean var8 = this.method_16((double)var3) || this.field_119;
      this.field_120.method_2(var7 ? 1.4e+02F : 0.0F, 250L);
      this.field_121.method_2(var8 && this.field_120.method_45() == 0.0F ? 1.4e+02F : 0.0F, 250L);
      if (this.field_120.method_45() > 0.0F) {
         if (this.field_118) {
            hud.method_334(this.field_116 ? Math.abs(var2 - var5) : var2);
         }

         int var9 = Colors.method_5(Color.black, (int)this.field_120.method_45());
         Render2D.method_9(RenderUtil.mioPose(var1.pose()), 0.0F, 0.0F, (float)hud.method_341(), (float)var6, var9);
         Render2D.method_9(RenderUtil.mioPose(var1.pose()), (float)(var5 - hud.method_341()), 0.0F, (float)var5, (float)var6, var9);
      } else if (this.field_121.method_45() > 0.0F) {
         if (this.field_119) {
            hud.method_82(this.field_117 ? Math.abs(var3 - var6) : var3);
         }

         int var10 = Colors.method_5(Color.black, (int)this.field_121.method_45());
         Render2D.method_9(RenderUtil.mioPose(var1.pose()), 0.0F, 0.0F, (float)var5, (float)hud.method_342(), var10);
         Render2D.method_9(RenderUtil.mioPose(var1.pose()), 0.0F, (float)(var6 - hud.method_342()), (float)var5, (float)var6, var10);
      }
   }

   public void method_2(double var1, double var3, int var5) {
      if (var5 == 0) {
         this.field_118 = this.getId(var1);
         if (!this.field_118) {
            this.field_119 = this.method_16(var3);
         }

         this.field_116 = var1 > (double)((float)mc.getWindow().getGuiScaledWidth() / 2F);
         this.field_117 = var3 > (double)((float)mc.getWindow().getGuiScaledHeight() / 2F);
      }
   }

   public void method_9(double var1, double var3, int var5) {
      if (var5 == 0) {
         this.field_118 = false;
         this.field_119 = false;
      }
   }

   public boolean getId(double var1) {
      return var1 < (double)(hud.method_341() + 1) || var1 > (double)(mc.getWindow().getGuiScaledWidth() - hud.method_341() - 1);
   }

   public boolean method_16(double var1) {
      return var1 < (double)(hud.method_342() + 1) || var1 > (double)(mc.getWindow().getGuiScaledHeight() - hud.method_342() - 1);
   }
}
