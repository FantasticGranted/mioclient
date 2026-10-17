package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.function.Predicate;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.ResizableHudElement;
import me.mioclient.internal.FontRenderer;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class AbstractModule_15 extends AbstractModule_26 {
   public final Setting<Boolean> white = this.add(new BooleanSetting("White", false));

   public AbstractModule_15() {
      super("Totems");
      this.method_2(new ResizableHudElement(this));
   }

   @Override
   public void method_2(GuiGraphicsExtractor var1) {
      ItemStack var2 = new ItemStack(Items.TOTEM_OF_UNDYING, InventoryUtil.method_29((Predicate<ItemStack>)(var0 -> var0.is(Items.TOTEM_OF_UNDYING))));
      if (var2.getCount() != 0) {
         var1.item(var2, 0, 0);
         if (var2.getCount() > 1) {
            String var3 = String.valueOf(var2.getCount());
            // TODO26.1 /* TODO26.1 flush */
            var1.pose().pushMatrix();
            var1.pose().translate(0.0F,  0.0F);
            FontRenderer.field_3143
               .method_9(
                  var1,
                  var3,
                  17F - FontRenderer.field_3143.method_221(var3),
                  9F,
                  this.white.getValue() ? Color.white : this.method_9(9F)
               );
            var1.pose().popMatrix();
         }
      }
   }

   @Override
   public float[] method_31() {
      return new float[]{16F, 16F};
   }
}
