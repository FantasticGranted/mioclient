package me.mioclient.internal;

import me.mioclient.api.SwapHandler;
import me.mioclient.record.IntPair;
import net.minecraft.world.item.Item;

public class Class_1160 implements SwapHandler {
   public Class_1160() {
      super();
   }

   @Override
   public void method_2(IntPair var1) {
      InventoryUtil.method_39(var1.getFirst());
   }

   @Override
   public void method_9(IntPair var1) {
      InventoryUtil.method_39(var1.getFirst());
   }

   @Override
   public IntPair method_31(Item var1) {
      int var2 = InventoryUtil.getId(InventoryUtil.method_5(var1));
      if (var2 == -1) {
         var2 = InventoryUtil.method_9(var1);
      }

      return new IntPair(var2, mc.player.getInventory().selected);
   }
}
