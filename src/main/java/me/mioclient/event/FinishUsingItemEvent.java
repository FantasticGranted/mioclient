package me.mioclient.event;

import me.mioclient.internal.Class_0082;
import net.minecraft.world.item.ItemStack;

/** Fired when the player finishes using an item. */
public final class FinishUsingItemEvent extends Class_0082 {
   public ItemStack field_2237;

   public FinishUsingItemEvent(ItemStack var1) {
      super();
      this.field_2237 = var1;
   }

   public ItemStack method_602() {
      return this.field_2237;
   }
}
