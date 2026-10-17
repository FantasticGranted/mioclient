package me.mioclient.record;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import me.mioclient.Hub;
import me.mioclient.internal.ItemUtil;
import me.mioclient.module.abstract_.AbstractModule_37;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

/** A container ItemStack and the most common item inside it (for tooltip previews). */
public final class ContainerPreview {
   public final ItemStack container;
   public final ItemStack mostCommon;
   public static final AbstractModule_37 tooltips = Hub.field_2595.method_78(AbstractModule_37.class);

   public ContainerPreview(ItemStack var1, ItemStack var2) {
      super();
      this.container = var1;
      this.mostCommon = var2;
   }

   public static ContainerPreview of(ItemStack var0) {
      if (!ItemUtil.method_29(var0.getItem())) {
         return null;
      } else {
         ItemContainerContents var1 = (ItemContainerContents)var0.get(DataComponents.CONTAINER);
         if (var1 == null) {
            return null;
         } else {
            HashMap<Item, Integer> var2 = new HashMap<>();
            List<ItemStack> var3 = var1.allItemsCopyStream().toList();

            for (int var4 = 0; var4 < var3.size(); var4++) {
               Item var5 = ((ItemStack)var3.get(var4)).getItem();
               var2.put(var5, 1 + var2.getOrDefault(var5, 0));
            }

            if (var2.isEmpty()) {
               return null;
            } else {
               Entry<Item, Integer> var6 = var2.entrySet().stream().max(Entry.comparingByValue()).orElse(null);
               return var6 == null ? null : new ContainerPreview(var0, ((Item)var6.getKey()).getDefaultInstance());
            }
         }
      }
   }

   public ItemStack getContainer() {
      return this.container;
   }

   public ItemStack getMostCommon() {
      return this.mostCommon;
   }
}
