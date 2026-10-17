package me.mioclient.internal;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.Nameable;
import me.mioclient.api.GuiComponent;
import me.mioclient.api.RegistryHolder;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.client.UIModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import com.mojang.blaze3d.vertex.PoseStack;

/**
 * A titled, draggable ClickGUI window that holds and lays out child components (module
 * buttons), with an expand/collapse animation, shadow and outline.
 */
public class ClickGuiPanel implements Nameable, GuiComponent, RegistryHolder<GuiComponent, List<GuiComponent>> {
   public final ArrayList<GuiComponent> field_2378;
   public final String field_2379;
   public final int field_1656;
   public int field_2380 = 14;
   public int field_2381;
   public int field_2382;
   public boolean field_111;
   public int field_1322;
   public int field_1321;
   public boolean field_2383;
   public int field_1657;
   public ToggleAnimation field_2384;

   public ClickGuiPanel(String var1) {
      super();
      this.field_2378 = new ArrayList<>();
      this.field_2380 = 14;
      this.field_2384 = new ToggleAnimation(() -> this.method_852().animSpeed.getValue() * 2F, true);
      this.field_2379 = var1;
      this.field_1656 = 92;
      this.field_2381 = 0;
      this.field_111 = true;
      this.field_1322 = 5;
      this.field_1321 = 5;
      this.field_2383 = false;
      this.field_2384.method_36(true);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, PoseStack var2, double var3, double var5) {
      Render2D.method_4(
         RenderUtil.mioPose(var1.pose()),
         (float)this.field_1321,
         (float)this.field_1322,
         (float)(this.field_1321 + this.method_216()),
         (float)(this.field_1322 + 14),
         UIModule.field_2843.color.getValue()
      );
      FontRenderer.field_3143
         .method_9(
            var1,
            this.field_2379,
            (float)(this.field_1321 + 2),
            (float)this.field_1322 + 7F - (float)FontRenderer.field_3143.method_66() / 2F,
            Color.white
         );
      this.field_2384.method_3(this.field_111);
      float var7 = this.field_2384.method_45();
      if (var7 != 1F && this.field_1657 != 0) {
         Render2D.method_474();
         ScissorStack.method_9(
            this.field_1321 - 1, this.field_1322 - 1, this.method_216() + 2, (int)(14F + (float)this.field_1657 * var7) + 3
         );
      }

      if (var7 > 0.0F && !this.field_2378.isEmpty()) {
         Render2D.method_4(
            RenderUtil.mioPose(var1.pose()),
            (float)this.field_1321,
            (float)(this.field_1322 + 14),
            (float)(this.field_1321 + this.method_216()),
            (float)(this.field_1322 + this.field_1657) + 0.5F,
            UIModule.field_2843.bgColor.getValue()
         );
         this.field_2378.forEach(var7x -> {
            if (this.method_2(var7x)) {
               var7x.method_4(var3, var5);
               var7x.method_2(var1, var2, var3, var5);
            }
         });
         if (UIModule.field_2843.windowShadow.getValue()) {
            Render2D.method_4(
               RenderUtil.mioPose(var1.pose()),
               (float)this.field_1321 + UIModule.field_2843.shadowSize.getValue(),
               (float)(this.field_1322 + this.field_1657) + 0.5F,
               (float)(this.field_1321 + this.method_216()),
               (float)(this.field_1322 + this.field_1657) + 0.5F + UIModule.field_2843.shadowSize.getValue(),
               UIModule.field_2843.shadowColor.getValue()
            );
            Render2D.method_4(
               RenderUtil.mioPose(var1.pose()),
               (float)(this.field_1321 + this.method_216()),
               (float)this.field_1322 + UIModule.field_2843.shadowSize.getValue(),
               (float)(this.field_1321 + this.method_216()) + UIModule.field_2843.shadowSize.getValue(),
               (float)(this.field_1322 + this.field_1657) + 0.5F + UIModule.field_2843.shadowSize.getValue(),
               UIModule.field_2843.shadowColor.getValue()
            );
         }
      }

      if (UIModule.field_2843.line.getValue()) {
         Render2D.method_29(
            RenderUtil.mioPose(var1.pose()),
            (float)(this.field_1321 - 1),
            (float)(this.field_1322 - 1),
            (float)(this.field_1321 + this.method_216()),
            (float)this.field_1322 + (var7 == 0.0F ? 14F : (float)this.field_1657 + 0.5F),
            UIModule.field_2843.color.getValue()
         );
      }

      if (var7 != 1F && this.field_1657 != 0) {
         Render2D.method_474();
         ScissorStack.method_57();
      }

      int var8 = 14 + 1;

      for (GuiComponent var10 : this.field_2378) {
         if (!this.method_2(var10)) {
            var10.method_26(0);
         } else {
            var10.method_26(var8);
            var8 += var10.method_66();
         }
      }

      this.field_1657 = var7 == 0.0F ? 15 : var8;
   }

   @Override
   public void method_2(double var1, double var3, int var5) {
      if (this.method_78(var1, var3) && var5 == 1) {
         this.method_80(!this.field_111);
      }

      if (this.field_111) {
         for (GuiComponent var7 : this.field_2378) {
            var7.method_2(var1, var3, var5);
         }
      }
   }

   @Override
   public void method_9(double var1, double var3, int var5) {
      if (this.field_111) {
         for (GuiComponent var7 : this.field_2378) {
            var7.method_9(var1, var3, var5);
         }
      }
   }

   @Override
   public void method_7(double var1, double var3, double var5) {
      if (this.field_111) {
         for (GuiComponent var8 : this.field_2378) {
            var8.method_7(var1, var3, var5);
         }
      }
   }

   @Override
   public void method_68(int var1) {
      if (this.field_111) {
         for (GuiComponent var3 : this.field_2378) {
            var3.method_68(var1);
         }
      }
   }

   @Override
   public void method_9(char var1) {
      if (this.field_111) {
         for (GuiComponent var3 : this.field_2378) {
            var3.method_9(var1);
         }
      }
   }

   @Override
   public void init() {
      if (this.field_111) {
         for (GuiComponent var2 : this.field_2378) {
            var2.init();
         }
      }
   }

   public ArrayList<GuiComponent> method_714() {
      return this.field_2378;
   }

   public void method_157(boolean var1) {
      if (var1 && !Hub.method_755().method_1000()) {
         Hub.method_755().method_294(true);
         this.field_2383 = true;
      } else {
         if (Hub.method_755().method_1000()) {
            Hub.method_755().method_294(false);
         }

         this.field_2383 = false;
      }
   }

   public boolean method_2(GuiComponent var1) {
      return true;
   }

   public boolean method_194() {
      return this.field_111 && this.field_2384.current == 2F;
   }

   public float method_715() {
      return this.field_2384.method_605() - 1F;
   }

   public void method_80(boolean var1) {
      this.field_111 = var1;
   }

   public void method_142() {
      int var1 = 14 + 1;

      for (GuiComponent var3 : this.field_2378) {
         var3.method_26(var1);
         var1 += var3.method_66();
      }

      this.field_1657 = var1;
   }

   public int getX() {
      return this.field_1321;
   }

   public void setX(int var1) {
      this.field_1321 = var1;
   }

   public int getY() {
      return this.field_1322;
   }

   public void setY(int var1) {
      this.field_1322 = var1;
   }

   public int method_216() {
      return this.field_1656 + UIModule.field_2843.frameWidth.getValue();
   }

   @Override
   public int method_66() {
      return this.field_1657;
   }

   public void method_22(int var1, int var2) {
      if (this.field_2383) {
         this.setX(Math.max(0, Math.min(MinecraftAccess.mc.getWindow().getGuiScaledWidth() - this.method_216(), var1 - this.field_2381)));
         this.setY(Math.max(0, Math.min(MinecraftAccess.mc.getWindow().getGuiScaledHeight() - this.method_66(), var2 - this.field_2382)));
      }
   }

   public boolean method_78(double var1, double var3) {
      return var1 >= (double)this.field_1321
         && var1 <= (double)(this.field_1321 + this.method_216())
         && var3 >= (double)this.field_1322
         && var3 <= (double)(this.field_1322 + 14);
   }

   @Override
   public boolean method_5(double var1, double var3) {
      return var1 >= (double)this.field_1321
         && var1 <= (double)(this.field_1321 + this.method_216())
         && var3 >= (double)this.field_1322
         && var3 <= (double)(this.field_1322 + this.field_1657);
   }

   public boolean method_192() {
      return this.field_2383;
   }

   @Override
   public String getName() {
      return this.field_2379;
   }

   public List<GuiComponent> getRegistry() {
      return this.field_2378;
   }

   public boolean method_9(GuiComponent var1) {
      return this.getRegistry().add(var1);
   }

   @Override public boolean register(GuiComponent var1)   { return method_9(var1); }
   @Override public boolean unregister(GuiComponent var1) { return this.getRegistry().remove(var1); }

   public boolean method_5(GuiComponent var1) {
      return this.getRegistry().add(var1);
   }
}
