package me.mioclient.internal;
import net.minecraft.world.item.ItemStack;

import me.mioclient.api.SwapHandler;
import me.mioclient.record.IntPair;
import net.minecraft.world.item.Item;

public class Class_0867 implements SwapHandler {
   public Class_0867() {
      super();
   }

   @Override
   public void method_2(IntPair var1) {
      InventoryUtil.method_5(var1.getFirst(), var1.getSecond());
   }

   @Override
   public void method_9(IntPair var1) {
      InventoryUtil.method_5(var1.getSecond(), var1.getFirst());
   }

   @Override
   public IntPair method_31(Item var1) {
      if (!Class_0985.method_38(var1)) {
         return IntPair.EMPTY;
      } else {
         int var2 = 6;
         net.minecraft.world.item.equipment.Equippable var3 = new ItemStack(var1).get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
         if (var3 != null && var3.slot().isArmor()) {
            var2 = 8 - var3.slot().getIndex();
         }

         int var4 = InventoryUtil.method_9(var1);
         return new IntPair(var4, var2);
      }
   }
}
