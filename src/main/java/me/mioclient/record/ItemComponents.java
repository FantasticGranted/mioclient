package me.mioclient.record;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.Item;

/** An Item together with the ComponentMap describing its data components. */
public final class ItemComponents {
   public final Item item;
   public final DataComponentMap components;

   public ItemComponents(Item var1, DataComponentMap var2) {
      super();
      this.item = var1;
      this.components = var2;
   }

   public Item getItem() {
      return this.item;
   }

   public DataComponentMap getComponents() {
      return this.components;
   }
}
