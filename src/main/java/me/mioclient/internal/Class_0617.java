package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.module.Category;
import me.mioclient.record.Pair;
import me.mioclient.record.Vec2i;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import com.mojang.blaze3d.opengl.GlStateManager;

public class Class_0617 extends ClickGui implements MinecraftAccess {
   public static final List<Class_0535> field_1950 = Collections.synchronizedList(new ArrayList<>());
   public static final Class_0255 field_1951 = new Class_0255();
   public final Class_0051 field_1952 = new Class_0051();
   public final ToggleAnimation field_1953 = new ToggleAnimation(2F, true);
   public boolean field_470;
   public float field_1954;
   public float field_1955;
   public float field_1956;
   public float field_1957;

   public Class_0617() {
      super();

      for (ScreenCorner var4 : ScreenCorner.values()) {
         field_1950.add(new Class_0535(var4));
      }

      ModuleCategoryPanel var5 = new ModuleCategoryPanel(Category.HUD);
      var5.method_714().sort(Comparator.comparing(var0 -> var0 instanceof ModulePanel var1 ? var1.method_65().getName() : ""));
      var5.method_216();
      var5.setX(85);
      var5.setY(10);
      this.field_3462.add(var5);
   }

   @Override
   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {

      field_1951.method_220(false);
      this.field_1956 = (float)mouseX;
      this.field_1957 = (float)mouseY;
      float var5 = (float)context.guiWidth() / 2F;
      float var6 = (float)context.guiHeight() / 2F;
      Color var7 = Colors.method_2(Color.white, 1.0F * 0.4F);
      Render2D.method_4(
         RenderUtil.mioPose(context.pose()),
         var5 - 0.5F,
         0.0F,
         var5 + 0.5F,
         var6 * 2F,
         var7
      );
      Render2D.method_4(
         RenderUtil.mioPose(context.pose()),
         0.0F,
         var6 - 0.5F,
         var5 - 0.5F,
         var6 + 0.5F,
         var7
      );
      Render2D.method_4(
         RenderUtil.mioPose(context.pose()),
         var5 + 0.5F,
         var6 - 0.5F,
         var5 * 2F,
         var6 + 0.5F,
         var7
      );
      this.field_1952.method_9(context, mouseX, mouseY, delta);
      this.method_2(context, (double)mouseX, (double)mouseY);
      if (this.field_470) {
         Class_0535 var8 = this.method_5(ScreenCorner.NONE);
         HudElement var9 = var8.method_566().method_606();
         float[] var10 = var9.method_193();
         var9.method_2(Math.max(0.0F, Math.min((float)context.guiWidth() - var10[0], (float)mouseX - this.field_1954)), true);
         var9.method_9(Math.max(0.0F, Math.min((float)context.guiHeight() - var10[1], (float)mouseY - this.field_1955)), true);
         var9.method_197();
         if (Math.abs(var9.method_59() + var10[0] / 2F - var5) <= 5F) {
            float var11 = var5 - var10[0] / 2F;
            var9.method_2(var11, true);
            this.method_2(context, (double)var11 - 0.5, true);
            this.method_2(context, (double)(var11 + var10[0]), true);
         }

         if (Math.abs(var9.method_60() + var10[1] / 2F - var6) <= 5F) {
            float var13 = var6 - var10[1] / 2F;
            var9.method_9(var13, true);
            this.method_2(context, (double)var13 - 0.5, false);
            this.method_2(context, (double)(var13 + var10[1]), false);
         }
      }

      float var12 = this.field_1953.method_45();
      this.field_1953.method_3(field_1951.method_290());
      if (field_1951.method_290() || (double)var12 > 0.1) {
         FontRenderer.field_3143
            .method_9(
               context,
               field_1951.method_286(),
               field_1951.method_59() + 1e+01F * (1F - var12) * (float)field_1951.method_288(),
               field_1951.method_60(),
               Colors.method_9(Color.white, (int)(var12 * 255F))
            );
      }

      super.extractRenderState(context, mouseX, mouseY, delta);
   }

   @Override
   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      Pair var6 = null;

      for (Class_0535 var8 : field_1950) {
         for (int var9 = var8.method_566().size() - 1; var9 >= 0; var9--) {
            HudElement var10 = var8.method_566().get(var9);
            if (var10.method_176() && button == 0 && var6 == null) {
               if (var10 instanceof ResizableHudElement) {
                  ResizableHudElement var11 = (ResizableHudElement)var10;
                  if (var11.method_39(mouseX, mouseY)) {
                     continue;
                  }
               }

               if (var10.method_5(mouseX, mouseY)) {
                  var6 = new Pair<>(var8.method_3(), var10);
                  this.field_1954 = (float)((int)(mouseX - (double)var10.method_59()));
                  this.field_1955 = (float)((int)(mouseY - (double)var10.method_60()));
                  this.field_470 = true;
                  var10.method_6(true);
               }
            }
         }
      }

      if (var6 != null) {
         this.method_5((ScreenCorner)var6.getLeft()).method_566().remove(var6.getRight());
         this.method_5(ScreenCorner.NONE).method_566().addLast((HudElement)var6.getRight());
         ((HudElement)var6.getRight()).method_2(ScreenCorner.NONE);
      } else {
         this.field_1952.method_2(mouseX, mouseY, button);
      }

      return super.mouseClicked(event, doubleClick);
   }

   @Override
   public boolean mouseReleased(MouseButtonEvent event) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      this.field_1952.method_9(mouseX, mouseY, button);
      if (this.field_470) {
         this.field_470 = false;
         Class_0535 var6 = this.method_5(ScreenCorner.NONE);
         HudElement var7 = var6.method_566().method_606();
         var7.method_6(false);
         var6.method_566().remove(var7);

         for (Class_0535 var9 : field_1950) {
            for (HudElement var11 : var9.method_566()) {
               if (var11.method_198().method_2(var7.method_198())) {
                  this.method_5(var9.method_3()).method_566().addLast(var7);
                  var7.method_2(var9.method_3());
                  return true;
               }
            }
         }

         for (ScreenCorner var15 : ScreenCorner.values()) {
            if (var15.method_198().method_2(var7.method_198().method_22(5F))) {
               this.method_5(var15).method_566().addLast(var7);
               var7.method_2(var15);
               return true;
            }
         }
      }

      return super.mouseReleased(event);
   }

   @Override
   public boolean keyPressed(KeyEvent event) {
      int keyCode = event.key();
      int scanCode = event.scancode();
      int modifiers = event.modifiers();
      boolean var4 = false;

      for (ClickGuiPanel var6 : this.field_3462) {
         if (var6.method_5((double)this.field_1956, (double)this.field_1957)) {
            var4 = true;
            break;
         }
      }

      if (!var4) {
         HudElement var7 = this.method_630();
         Vec2i var8 = this.method_632(keyCode);
         if (var8 != null && var7 != null && var7.method_176()) {
            var7.method_2(var7.method_59() + (float)var8.method_401(), true);
            var7.method_9(var7.method_60() + (float)var8.getY(), true);
            var7.method_197();
         } else {
            switch (keyCode) {
               case 73:
                  if (!(Hub.method_755().method_454() instanceof StringSettingWidget)) {
                     this.method_2(ScreenCorner.BOTTOM_LEFT, ScreenCorner.BOTTOM_RIGHT);
                     this.method_2(ScreenCorner.TOP_LEFT, ScreenCorner.TOP_RIGHT);
                  }
                  break;
               case 259:
               case 261:
                  if (var7 != null && var7.method_176()) {
                     var7.method_199().method_38(false);
                  }
            }
         }
      }

      return super.keyPressed(event);
   }

   @Override
   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   @Override
   public void close() {
      super.close();
      if (this.field_470) {
         this.method_5(ScreenCorner.NONE).method_566().method_606().method_6(false);
         this.field_470 = false;
      }
   }

   public void method_2(ScreenCorner var1, HudElement var2) {
      this.method_5(var1).method_566().add(var2);
   }

   public void method_9(HudElement var1) {
      this.method_2(ScreenCorner.NONE, var1);
   }

   public void method_5(HudElement var1) {
      for (Class_0535 var3 : this.method_629()) {
         if (var3.method_566().remove(var1)) {
            return;
         }
      }
   }

   public List<Class_0535> method_629() {
      return field_1950;
   }

   public void method_2(GuiGraphicsExtractor var1, double var2, double var4) {
      for (Class_0535 var7 : this.method_629()) {
         var7.method_2(var1, RenderUtil.mioPose(var1.pose()), var2, var4);
      }

      Render2D.method_474();
   }

   public Class_0535 method_5(ScreenCorner var1) {
      for (Class_0535 var3 : this.method_629()) {
         if (var3.method_3() == var1) {
            return var3;
         }
      }

      return null;
   }

   public void method_2(ScreenCorner var1, ScreenCorner var2) {
      Class_0535 var3 = this.method_5(var1);
      Class_0535 var4 = this.method_5(var2);
      ArrayList var5 = new ArrayList<>(var3.method_566());
      var3.method_566().clear();

      for (HudElement var7 : var4.method_566()) {
         var3.method_566().add(var7);
      }

      var4.method_566().clear();

      for (HudElement var9 : (Iterable<HudElement>)(Iterable<?>)(var5)) {
         var4.method_566().add(var9);
      }
   }

   public HudElement method_630() {
      for (Class_0535 var2 : field_1950) {
         for (HudElement var4 : var2.method_566()) {
            if (var4.method_5((double)this.field_1956, (double)this.field_1957)) {
               return var4;
            }
         }
      }

      return null;
   }

   public void method_2(GuiGraphicsExtractor var1, double var2, boolean var4) {
      float var5 = (float)var2;
      if (var4) {
         Render2D.method_4(RenderUtil.mioPose(var1.pose()), var5, 0.0F, var5 + 0.5F, (float)var1.guiHeight(), Color.yellow);
      } else {
         Render2D.method_4(RenderUtil.mioPose(var1.pose()), 0.0F, var5, (float)var1.guiWidth(), var5 + 0.5F, Color.yellow);
      }
   }

   @Override
   public void method_38(GuiGraphicsExtractor var1) {
   }

   @Override
   public boolean method_631() {
      return false;
   }

   public Vec2i method_632(int var1) {
      return switch (var1) {
         case 262 -> new Vec2i(1, 0);
         case 263 -> new Vec2i(-1, 0);
         case 264 -> new Vec2i(0, 1);
         case 265 -> new Vec2i(0, -1);
         default -> null;
      };
   }
}
