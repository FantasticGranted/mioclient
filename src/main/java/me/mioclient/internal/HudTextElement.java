package me.mioclient.internal;

import java.awt.Color;
import java.util.function.Supplier;
import me.mioclient.Hub;
import me.mioclient.module.client.HUDModule;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

/** Animated HUD text entry: draws a Supplier-provided Text with smoothed width and fade-in. */
public class HudTextElement {
   public static HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   public final Supplier<Component> field_357;
   public final Supplier<Boolean> field_358;
   public float field_359;
   public final AnimatedValue field_360 = new AnimatedValue(2F, true);

   public HudTextElement(Supplier<Component> var1, Supplier<Boolean> var2) {
      super();
      this.field_357 = var1;
      this.field_358 = var2;
   }

   public void method_2(GuiGraphicsExtractor var1, float var2, float var3, Color var4) {
      float var5 = FontRenderer.field_3143.method_221(this.method_15().getString());
      if (hud.smoothWidth.getValue()) {
         this.field_360.method_3(var5);
      } else {
         this.field_360.method_36(var5);
      }

      if (this.field_359 > 0.0F) {
         Color var6 = Colors.method_9(
            var4,
            (int)(
               (double)var4.getAlpha()
                  * Mth.clamp(
                     (double)(this.field_359 * 2F),
                     0.1,
                     1.0
                  )
            )
         );
         FontRenderer.field_3143.method_9(var1, this.method_15(), var2, var3, var6);
      }
   }

   public void method_142() {
      float var1 = 1F / (float)(Hub.field_2601.method_814() >> 2);
      if (this.isDrawn()) {
         this.field_359 += var1;
      } else {
         this.field_359 -= var1;
      }

      this.field_359 = Mth.clamp(this.field_359, 0.0F, 1F);
   }

   public float method_143() {
      return this.field_359;
   }

   public boolean isDrawn() {
      return this.field_358.get();
   }

   public Component method_15() {
      return this.field_357.get();
   }
}
