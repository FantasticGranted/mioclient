package me.mioclient.internal;

import java.util.List;
import me.mioclient.record.EnchantmentLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;

public final class Class_0714 {
   public static final List<EnchantmentLevel> field_2265 = List.of(
      new EnchantmentLevel(Enchantments.PROTECTION, 4), new EnchantmentLevel(Enchantments.UNBREAKING, 3), new EnchantmentLevel(Enchantments.MENDING, 1)
   );
   public static final List<EnchantmentLevel> field_2266 = List.of(
      new EnchantmentLevel(Enchantments.PROTECTION, 4), new EnchantmentLevel(Enchantments.UNBREAKING, 3), new EnchantmentLevel(Enchantments.MENDING, 1)
   );
   public static final List<EnchantmentLevel> field_2267 = List.of(
      new EnchantmentLevel(Enchantments.BLAST_PROTECTION, 4), new EnchantmentLevel(Enchantments.UNBREAKING, 3), new EnchantmentLevel(Enchantments.MENDING, 1)
   );
   public static final List<EnchantmentLevel> field_2268 = List.of(
      new EnchantmentLevel(Enchantments.PROTECTION, 4),
      new EnchantmentLevel(Enchantments.UNBREAKING, 3),
      new EnchantmentLevel(Enchantments.MENDING, 1),
      new EnchantmentLevel(Enchantments.FEATHER_FALLING, 4)
   );
   public static final List<EnchantmentLevel> field_2269 = List.of(new EnchantmentLevel(Enchantments.UNBREAKING, 3), new EnchantmentLevel(Enchantments.MENDING, 1));
   public static final List<EnchantmentLevel> field_2270 = List.of(
      new EnchantmentLevel(Enchantments.SHARPNESS, 5),
      new EnchantmentLevel(Enchantments.FIRE_ASPECT, 2),
      new EnchantmentLevel(Enchantments.KNOCKBACK, 2),
      new EnchantmentLevel(Enchantments.SWEEPING_EDGE, 3),
      new EnchantmentLevel(Enchantments.UNBREAKING, 3),
      new EnchantmentLevel(Enchantments.MENDING, 1)
   );

   public Class_0714() {
      super();
   }

   public static boolean method_9(ItemStack var0) {
      net.minecraft.world.item.equipment.Equippable var1 = var0.get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
      if (var1 != null && var1.slot().isArmor() && !var0.is(Items.ELYTRA)) {
         return switch (var1.slot()) {
            case MAINHAND, OFFHAND -> false;
            case FEET -> method_2(var0, field_2268);
            case LEGS -> method_2(var0, field_2267);
            case CHEST -> method_2(var0, field_2266);
            case HEAD -> method_2(var0, field_2265);
            case BODY -> false;
            default -> throw new MatchException(null, null);
         };
      } else if (InventoryUtil.isSword(var0)) {
         return method_2(var0, field_2270);
      } else {
         return var0.is(Items.ELYTRA) ? method_2(var0, field_2269) : false;
      }
   }

   public static boolean method_2(ItemStack var0, Iterable<EnchantmentLevel> var1) {
      for (EnchantmentLevel var3 : var1) {
         if (EnchantmentUtil.method_2(var3.getEnchantment(), var0) != var3.method_473()) {
            return false;
         }
      }

      return true;
   }
}
