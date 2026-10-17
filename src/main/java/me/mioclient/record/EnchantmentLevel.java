package me.mioclient.record;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

/** An enchantment (by RegistryKey) together with its level. */
public final class EnchantmentLevel {
   public final ResourceKey<Enchantment> enchantment;
   public final int level;

   public EnchantmentLevel(ResourceKey<Enchantment> var1, int var2) {
      super();
      this.enchantment = var1;
      this.level = var2;
   }

   public ResourceKey<Enchantment> getEnchantment() {
      return this.enchantment;
   }

   public int method_473() {
      return this.level;
   }
}
