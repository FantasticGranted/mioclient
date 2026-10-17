package me.mioclient.internal;

import me.mioclient.api.SwapHandler;
import me.mioclient.record.IntPair;
import net.minecraft.world.item.Item;

public class Class_0891 implements SwapHandler {
   public Class_0891() {
      super();
   }

   @Override
   public void method_2(IntPair var1) {
      InventoryUtil.method_78(var1.getFirst());
   }

   @Override
   public void method_9(IntPair var1) {
      InventoryUtil.method_78(var1.getSecond());
   }

   @Override
   public IntPair method_31(Item var1) {
      int var2 = InventoryUtil.method_5(var1);
      return new IntPair(var2, mc.player.getInventory().selected);
   }
}
