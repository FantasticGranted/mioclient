package me.mioclient.module.abstract_;

import java.awt.Color;
import me.mioclient.Hub;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.ToggleAnimation;
import me.mioclient.internal.FontRenderer;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class AbstractModule_29 extends AbstractModule_26 {
   public final Setting<Color> color = this.add(new ColorSetting("Color", Color.gray));
   public final ToggleAnimation field_2894 = new ToggleAnimation(1F);

   public AbstractModule_29() {
      super("Lag'O'Meter", "lag", "lagometer");
      this.method_2(new ResizableHudElement(this));
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      String var2 = this.method_4();
      this.field_2894.method_3(var2 != null);
      if (var2 != null) {
         float var3 = this.field_2894.method_45();
         FontRenderer.field_3143
            .method_9(var1, var2, 0.0F, (1F - var3) * (float)(-FontRenderer.field_3143.method_66()), this.color.getValue());
      }
   }

   @Override
   public float[] method_31() {
      String var1 = this.method_4();
      return var1 == null ? new float[]{0.0F, 0.0F} : new float[]{FontRenderer.field_3143.method_221(var1), (float)(FontRenderer.field_3143.method_66() + 1)};
   }

   public String method_4() {
      long var1 = Math.max(System.currentTimeMillis() - Hub.field_2602.method_987(), 0L);
      return (var1 <= 1000L || mc.isLocalServer()) && !this.method_22()
         ? null
         : "The server is not responding for %.1fs"
            .formatted((float)(System.currentTimeMillis() - Hub.field_2602.method_987()) / 1e+03F);
   }
}
