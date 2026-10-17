package me.mioclient.enum_;

import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.EnchantmentUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;

/** Aura mace attack mode: none, breach, density, or smart. */
public enum MaceMode implements Nameable {
   NONE("None", var0 -> false),
   BREACH("Breach", var0 -> EnchantmentUtil.method_9(Enchantments.BREACH, var0)),
   DENSITY("Density", var0 -> EnchantmentUtil.method_9(Enchantments.DENSITY, var0) && MinecraftAccess.mc.player.fallDistance > 3.0F),
   SMART(
      "Smart",
      var0 -> !(MinecraftAccess.mc.player.fallDistance > 3.0F) && Hub.field_2615.method_1161() > 1
            ? EnchantmentUtil.method_9(Enchantments.BREACH, var0)
            : EnchantmentUtil.method_9(Enchantments.DENSITY, var0)
   );

   public final Predicate<ItemStack> field_2804;
   public final String field_2805;

    MaceMode(String var3, Predicate<ItemStack> var4) {
      this.field_2804 = var4;
      this.field_2805 = var3;
   }

   @Override
   public String getName() {
      return this.field_2805;
   }

   public boolean method_16(ItemStack var1) {
      return this.field_2804.test(var1);
   }
}
