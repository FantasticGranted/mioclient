package me.mioclient.module.abstract_;

import java.awt.Color;
import me.mioclient.internal.TimedAnimation;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.RenderUtil;
import me.mioclient.internal.Colors;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class AbstractModule_33 extends AbstractModule_26 {
   public final Setting<Color> background = this.add(new ColorSetting("Background", new Color(10, 10, 10, 50)));
   public final Setting<Color> outline = this.add(new ColorSetting("Outline", new Color(10, 10, 10, 100)));
   public final Setting<Boolean> hideEmpty = this.add(new BooleanSetting("HideEmpty", true));
   public final TimedAnimation field_2555 = new TimedAnimation();

   public AbstractModule_33() {
      super("Inventory");
      ResizableHudElement var1 = new ResizableHudElement(this);
      var1.method_2(this);
      this.method_2(var1);
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      this.field_2555.method_2(!this.isEmpty(), 200L);
      float var2 = this.field_2555.method_45();
      if (!this.hideEmpty.getValue()) {
         var2 = 1F;
      }

      Color var3 = Colors.method_2(this.background.getValue(), (float)this.background.getValue().getAlpha() / 255F * var2);
      Color var4 = Colors.method_2(this.outline.getValue(), (float)this.outline.getValue().getAlpha() / 255F * var2);
      RenderUtil.field_2672.method_9(RenderUtil.mioPose(var1.pose()), 0.0F, 0.0F, 162F, 54F, var3);
      RenderUtil.field_2672
         .method_2(
            RenderUtil.mioPose(var1.pose()),
            -1F,
            -1F,
            162F,
            54F,
            var4
         );
      NonNullList var5 = mc.player.getInventory().items;

      for (int var6 = 0; var6 < var5.size() - 9; var6++) {
         int var7 = var6 % 9 * 18;
         int var8 = var6 / 9 * 18;
         ItemStack var9 = (ItemStack)var5.get(var6 + 9);
         var1.item(var9, var7, var8);
         var1.itemDecorations(mc.font, var9, var7, var8);
      }
   }

   public boolean isEmpty() {
      NonNullList var1 = mc.player.getInventory().items;

      for (int var2 = 0; var2 < var1.size() - 9; var2++) {
         ItemStack var3 = (ItemStack)var1.get(var2 + 9);
         if (!var3.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   @Override
   public float[] method_31() {
      return new float[]{162F, 54F};
   }
}
