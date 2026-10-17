package me.mioclient.internal;

import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.file.Path;
import java.util.List;
import me.mioclient.enum_.Class_0836;
import net.minecraft.client.gui.GuiGraphicsExtractor;

/** Main ClickGui screen with category/config tabs (modules, config, …). */
public class TabbedClickGui extends ClickGui {
   public final Class_0040 field_1034 = new Class_0040(this);
   public final Class_0516 field_1035 = new Class_0516(this);
   public Class_0836 field_1036 = Class_0836.MODULES;
   public final TimedAnimation field_1037 = new TimedAnimation();

   public TabbedClickGui() {
      super();
      this.field_3462.add(this.field_1034);
      this.field_1035.setX(this.field_1034.getX() + this.field_1034.method_216() + 3);
      this.field_3462.add(this.field_1035);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      this.field_1037.method_2(!mc.isWindowActive(), 250L);
      float var5 = this.field_1037.method_45();
      if (var5 != 0.0F) {
         // TODO26.1 RenderSystem.setShaderColor(
         // TODO26.1 1F, 1F, 1F, 0.75F * var5
         // TODO26.1 );
         var1.centeredText(
            mc.font, "Drop preset files here", var1.guiWidth() / 2, var1.guiHeight() / 2, -1
         );
         // TODO26.1 RenderSystem.setShaderColor(
         // TODO26.1 1F, 1F, 1F, 1F
         // TODO26.1 );
      }
   }

   public void filesDragged(List<Path> paths) {
      Class_0227.method_2(this, paths);
   }

   public Class_0040 method_367() {
      return this.field_1034;
   }

   public Class_0516 method_368() {
      return this.field_1035;
   }

   public Class_0836 method_369() {
      return this.field_1036;
   }

   public void method_9(Class_0836 var1) {
      this.field_1036 = var1;
   }
}
