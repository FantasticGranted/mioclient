package me.mioclient.enum_;

import java.awt.Color;
import me.mioclient.api.Nameable;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.Colors;
import me.mioclient.module.client.HUDModule;
import me.mioclient.setting.ColorSetting;

/** HUD fade mode. */
public enum HudFadeMode implements Nameable {
   NONE("None") {
      @Override
      public Color method_2(HUDModule var1, float var2) {
         return ((ColorSetting)var1.color).method_132()
            ? ((ColorSetting)var1.color)
               .method_2(var1.color.getValue(), (int)(var2 * (float)var1.fadeOffset.getValue().intValue() * HudFadeMode.method_584()))
            : var1.color.getValue();
      }
   },
   PULSE("Pulse") {
      @Override
      public Color method_2(HUDModule var1, float var2) {
         return Colors.method_2(
            var1.color.getValue(),
            var1.secondColor.getValue(),
            2000.0,
            (double)(var2 * (float)var1.fadeOffset.getValue().intValue() * HudFadeMode.method_584())
         );
      }
   },
   RAW("Raw") {
      @Override
      public Color method_2(HUDModule var1, float var2) {
         int var3 = var1.colorAmount.getValue();
         int var4 = var1.fadeOffset.getValue();
         float var5 = 1.0F / (float)var4;
         int var6 = (int)(
            (float)(System.currentTimeMillis() / (long)var1.fadeSpeed.getValue().intValue() % (long)(var3 * var4)) + var2 / HudFadeMode.method_584()
         );
         int var7 = (int)Math.floor((double)((float)var6 * var5));
         if (var7 < 0) {
            var7 = 0;
         }

         return var1.mixColors.getValue()
            ? Colors.method_2(
               var1.field_967.get(var7 % var3).getValue(), var1.field_967.get((var7 + 1) % var3).getValue(), 1.0F - ((float)var6 * var5 - (float)var7)
            )
            : var1.field_967.get(var7 % var3).getValue();
      }
   };

   public final String field_1770;

    HudFadeMode(String var3) {
      this.field_1770 = var3;
   }

   @Override
   public String getName() {
      return this.field_1770;
   }

   public abstract Color method_2(HUDModule var1, float var2);

   public static float method_584() {
      return 10.0F / (float)(FontRenderer.field_3143.method_66() + 2);
   }
}
