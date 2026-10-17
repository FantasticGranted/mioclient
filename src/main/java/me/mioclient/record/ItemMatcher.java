package me.mioclient.record;

import java.util.Objects;
import me.mioclient.internal.Class_0447;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/** Matches an ItemStack by raw item id plus a custom-name key. */
public final class ItemMatcher {
   public final int rawId;
   public final String customName;

   public ItemMatcher(int var1, String var2) {
      super();
      this.rawId = var1;
      this.customName = var2;
   }

   public boolean method_16(ItemStack var1) {
      return Item.getId(var1.getItem()) == this.rawId && Objects.equals(Class_0447.method_31(var1), this.customName);
   }

   public int getRawId() {
      return this.rawId;
   }

   public String getCustomName() {
      return this.customName;
   }
}
