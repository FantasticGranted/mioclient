package me.mioclient.internal;

import me.mioclient.internal.RenderUtil;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Season;
import me.mioclient.enum_.CursorShape;
import me.mioclient.module.client.UIModule;
import me.mioclient.module.render.BlurModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/**
 * The main ClickGUI screen: renders and manages draggable module panels, blur/tint/gradient
 * background, winter snow effect, hover tooltips and an open/close fade animation.
 */
public class ClickGui extends Screen implements MinecraftAccess {
   public static BlurModule blur = Hub.field_2595.method_78(BlurModule.class);
   public static CursorShape field_3460 = CursorShape.STANDARD;
   public static final int field_3461 = 25;
   public final ArrayList<ClickGuiPanel> field_3462 = new ArrayList<>();
   public final List<Class_0325> snowParticles = new ArrayList<>();
   public final AnimatedValue animMouseX = new AnimatedValue(2.5F, true);
   public final AnimatedValue animMouseY = new AnimatedValue(2.5F, true);
   public boolean field_2383 = false;
   public float field_1096;
   public boolean closing;
   public String tooltip = "";
   public long tooltipTime = 0L;
   public long animStartTime;
   public Screen field_2898;
   public boolean keyPressed;

   public ClickGui() {
      super(Component.literal("mio"));
      this.method_996();
   }

   public void init() {
      int var1 = mc.getWindow().getGuiScaledWidth();
      int var2 = mc.getWindow().getGuiScaledHeight();
      if (this.method_830() != null) {
         this.method_830().init(var1, var2);
      }

      if (!this.method_535()) {
         this.animMouseX.method_36((float)var1 / 2F);
         this.animMouseY.method_36((float)var2 / 2F);
      }

      this.field_2383 = false;
      this.method_999().forEach(var0 -> var0.method_157(false));
      this.field_1096 = UIModule.field_2843.guiScale.getValue();
      this.closing = false;
      this.animStartTime = System.currentTimeMillis();

      for (ClickGuiPanel var4 : this.field_3462) {
         var4.init();
      }
   }

   public void extractRenderState(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
      this.animMouseX.method_3((float)mouseX);
      this.animMouseY.method_3((float)mouseY);
      float var5 = this.animMouseX.method_45();
      float var6 = this.animMouseY.method_45();
      if (this.method_830() instanceof TitleScreen || this.method_830() instanceof JoinMultiplayerScreen) {
         GlStateManager._enablePolygonOffset();
         GlStateManager._polygonOffset(1F, 1e+03F);
         this.method_830().extractRenderState(context, -99, -99, delta);
         mc.renderBuffers().bufferSource().endBatch();
         GlStateManager._polygonOffset(1F, -1e+03F);
         GlStateManager._disablePolygonOffset();
      }

      field_3460.method_580();
      field_3460 = CursorShape.STANDARD;
      this.snowParticles.removeIf(Class_0325::method_381);
      if (this.snowParticles.size() < 25) {
         this.snowParticles.add(new Class_0325());
      }


      float var7 = (float)UiScaleHook.method_78(this.field_1096);
      UiScaleHook.method_38(this.field_1096);
      context.pose().scale(var7, var7);
      mouseX = (int)((float)mouseX / var7);
      mouseY = (int)((float)mouseY / var7);
      var5 /= var7;
      var6 /= var7;
      this.tooltip = "";
      if (!this.method_1001() && blur.isToggled()) {
         MioFramebuffer.method_2(() -> {
            for (ClickGuiPanel var3 : this.method_999()) {
               float var4 = var3.method_715();
               float var5x = (float)(var3.method_66() - 14) * var4;
               context.fill(var3.getX(), var3.getY(), var3.getX() + var3.method_216(), (int)((float)(var3.getY() + 14) + var5x), -1);
            }
         }, (float)blur.method_730());
      }

      this.method_38(context);
      if (Season.WINTER.method_29(LocalDate.now().getMonthValue())) {
         for (Class_0325 var9 : this.snowParticles) {
            var9.method_2(context);
         }
      }

      for (ClickGuiPanel var27 : this.field_3462) {
         var27.method_22(mouseX, mouseY);
         var27.method_2(context, RenderUtil.mioPose(context.pose()), (double)mouseX, (double)mouseY);
      }

      if (!this.tooltip.isBlank()) {
         Render2D.method_474();
         String[] var26 = this.tooltip.split("\n");
         float var28 = 0.0F;

         for (String var13 : var26) {
            float var14 = FontRenderer.field_3143.method_221(var13);
            if (var14 > var28) {
               var28 = var14;
            }
         }

         float var29 = var5 + 9F;
         float var30 = var5 + var28 + 1e+01F;
         if (var30 > (float)mc.getWindow().getGuiScaledWidth()) {
            var29 = var5 - var28 - 2F;
            var30 = var5 - 1F;
         }

         float var31 = var29;
         float var32 = var30;
         int var33 = (int)var6;
         float var15 = var6
            - 1F
            + ((float)FontRenderer.field_3143.method_66() + 1F) * (float)var26.length;
         MioFramebuffer.method_2(
            () -> RenderUtil.field_2672
                  .method_2(RenderUtil.mioPose(context.pose()), var31, (float)var33 - 1F, var32, var15, Colors.method_2(10, 10, 10, 140)),
            6F
         );
         RenderUtil.field_2672.method_9(RenderUtil.mioPose(context.pose()), var29, var6 - 1F, var30, var15, new Color(10, 10, 10, 80));
         int var16 = 0;

         for (String var20 : var26) {
            FontRenderer.field_3143
               .method_9(
                  context,
                  var20,
                  var29 + 1F,
                  var6 + ((float)FontRenderer.field_3143.method_66() + 1F) * (float)var16,
                  Color.white
               );
            var16++;
         }

         this.tooltipTime = System.currentTimeMillis();
      } else if (!UIModule.field_2843.constantReset.getValue() && this.tooltipTime != 0L && System.currentTimeMillis() > this.tooltipTime + 750L) {
         this.tooltipTime = 0L;
         ModulePanel.method_69();
      }

      this.method_2(context, mouseX, mouseY, delta);
      Render2D.method_474();
      // TODO26.1 RenderSystem.setShaderColor(
      // TODO26.1 1F, 1F, 1F, 1F
      // TODO26.1 );
      UiScaleHook.method_159();
   }

   public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      if (this.closing) {
         return false;
      } else {
         mouseX /= UiScaleHook.method_78(this.field_1096);
         mouseY /= UiScaleHook.method_78(this.field_1096);
         boolean var6 = super.mouseClicked(event, doubleClick);
         ClickGuiPanel var7 = null;

         for (int var8 = this.field_3462.size() - 1; var8 >= 0; var8--) {
            ClickGuiPanel var9 = this.field_3462.get(var8);
            if (var9.method_78(mouseX, mouseY) && button == 0 && var7 == null) {
               var7 = var9;
               var9.method_157(true);
               var9.field_2381 = (int)(mouseX - (double)var9.getX());
               var9.field_2382 = (int)(mouseY - (double)var9.getY());
            }

            var9.method_2(mouseX, mouseY, button);
         }

         if (var7 != null) {
            this.field_3462.remove(var7);
            this.field_3462.add(var7);
         }

         this.reset();
         this.method_5(mouseX, mouseY, button);
         return var6;
      }
   }

   public boolean mouseReleased(MouseButtonEvent event) {
      double mouseX = event.x();
      double mouseY = event.y();
      int button = event.button();
      mouseX /= UiScaleHook.method_78(this.field_1096);
      mouseY /= UiScaleHook.method_78(this.field_1096);

      for (int var6 = this.field_3462.size() - 1; var6 >= 0; var6--) {
         ClickGuiPanel var7 = this.field_3462.get(var6);
         var7.method_157(false);
         var7.method_9(mouseX, mouseY, button);
      }

      this.method_7(mouseX, mouseY, button);
      return super.mouseReleased(event);
   }

   public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
      double var9 = UiScaleHook.method_78(this.field_1096);
      mouseX /= var9;
      mouseY /= var9;
      int var11 = 2 + FontRenderer.field_3143.method_66() + UIModule.field_2843.buttonHeight.getValue();
      int var12 = this.field_3462.stream().mapToInt(ClickGuiPanel::method_66).max().orElse(var11);
      if ((double)var12 > (double)(mc.getWindow().getGuiScaledHeight() + 5) / var9) {
         var12 -= (int)((double)(mc.getWindow().getGuiScaledHeight() - 5) / var9);
      } else {
         var12 = (int)(-5.0 / var9);
      }

      if (verticalAmount != 0.0) {
         int var13 = (int)(verticalAmount * (double)var11);

         for (ClickGuiPanel var15 : this.field_3462) {
            if (!var15.method_192()) {
               var15.method_7(mouseX, mouseY, verticalAmount);
               int var16 = var15.getY() + var13;
               if (this.method_631()) {
                  var16 = Mth.clamp(var16, -var12, 5);
               }

               var15.setY(var16);
            }
         }
      }

      this.method_29(mouseX, mouseY, verticalAmount);
      return super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
   }

   public boolean charTyped(CharacterEvent event) {
      char chr = (char) event.codepoint();
      int modifiers = 0;
      for (int var3 = this.field_3462.size() - 1; var3 >= 0; var3--) {
         this.field_3462.get(var3).method_9(chr);
      }

      return super.charTyped(event);
   }

   public void tick() {
      if (this.closing && System.currentTimeMillis() >= this.animStartTime + 150L) {
         for (ClickGuiPanel var2 : this.field_3462) {
            var2.method_157(false);
         }

         if (this.field_2898 != null) {
            mc.setScreen(this.field_2898);
         } else {
            this.close();
         }

         this.method_5(null);
      }
   }

   public boolean keyPressed(KeyEvent event) {
      int keyCode = event.key();
      int scanCode = event.scancode();
      int modifiers = event.modifiers();
      this.keyPressed = true;

      for (int var4 = this.field_3462.size() - 1; var4 >= 0; var4--) {
         this.field_3462.get(var4).method_68(keyCode);
      }

      if (keyCode == 256 && this.keyPressed) {
         this.method_257();
      }

      return false;
   }

   public boolean shouldPause() {
      return false;
   }

   public void method_2(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
   }

   public void method_5(double var1, double var3, int var5) {
   }

   public void method_7(double var1, double var3, int var5) {
   }

   public void method_29(double var1, double var3, double var5) {
   }

   public void close() {
      super.onClose();
      CursorShape.STANDARD.method_580();
   }

   public void reset() {
      ModulePanel.method_69();
      this.tooltipTime = 0L;
   }

   public void method_996() {
      this.snowParticles.clear();

      for (int var1 = 0; var1 < 25; var1++) {
         this.snowParticles.add(new Class_0325());
      }
   }

   public float method_937() {
      float var1 = (float)(System.currentTimeMillis() - this.animStartTime) / 1.5e+02F;
      if (this.closing) {
         var1 = 1F - var1;
      }

      return Mth.clamp(var1, 0.0F, 1F);
   }

   public void method_997() {
      this.method_996();
      this.field_3462.sort(Comparator.comparing(var0 -> var0 instanceof ModuleCategoryPanel var1x ? var1x.field_37.ordinal() : 100));
      int var1 = 10;

      for (ClickGuiPanel var3 : this.field_3462) {
         var3.setX(var1);
         var1 += var3.method_216() + 3;
      }
   }

   public void method_38(GuiGraphicsExtractor var1) {
      var1.fill(0, 0, mc.getWindow().getGuiScaledWidth(), mc.getWindow().getGuiScaledHeight(), UIModule.field_2843.tint.getValue().hashCode());
      var1.fillGradient(
         0,
         0,
         mc.getWindow().getGuiScaledWidth(),
         mc.getWindow().getGuiScaledHeight(),
         Colors.method_5(UIModule.field_2843.gradientColor.getValue(), 0),
         UIModule.field_2843.gradientColor.getValue().hashCode()
      );
   }

   public void method_257() {
      if (!this.closing) {
         this.field_2383 = false;
         this.animStartTime = System.currentTimeMillis();
         if (UIModule.field_2843.isToggled()) {
            UIModule.field_2843.method_68();
         }
      } else {
         this.close();
      }

      this.closing = true;
   }

   public void method_998() {
      this.tooltipTime = System.currentTimeMillis();
   }

   public void method_316(String var1) {
      this.tooltip = var1;
   }

   public ArrayList<ClickGuiPanel> method_999() {
      return this.field_3462;
   }

   public boolean method_1000() {
      return this.field_2383;
   }

   public void method_294(boolean var1) {
      this.field_2383 = var1;
   }

   public boolean method_1001() {
      return this.closing;
   }

   public void method_1002() {
      if (mc.screen instanceof ClickGui var1) {
         this.method_5(var1.field_2898);
      } else {
         this.method_5(mc.screen);
      }

      mc.setScreen(this);
   }

   public boolean method_631() {
      return true;
   }

   public void method_5(Screen var1) {
      this.field_2898 = var1;
   }

   public Screen method_830() {
      return this.field_2898;
   }

   public void method_1003() {
      this.keyPressed = false;
   }
}
