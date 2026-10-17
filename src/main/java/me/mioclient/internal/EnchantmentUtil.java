package me.mioclient.internal;

import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

/**
 * Static helpers for reading enchantment levels off items / equipment slots and building
 * an enchantment-to-level map. Non-instantiable.
 */
public final class EnchantmentUtil implements MinecraftAccess {
   public EnchantmentUtil() {
      super();
      throw new IllegalArgumentException("Illegal constructor call");
   }

   public static int method_2(ResourceKey<Enchantment> var0, ItemStack var1) {
      if (var1.isEmpty()) {
         return 0;
      } else {
         for (Entry var3 : var1.getEnchantments().entrySet()) {
            if (((Holder)var3.getKey()).is(var0)) {
               return var3.getIntValue();
            }
         }

         return 0;
      }
   }

   public static int method_2(ResourceKey<Enchantment> var0, EquipmentSlot var1, LivingEntity var2) {
      return method_2(var0, var2.getItemBySlot(var1));
   }

   public static int method_2(ResourceKey<Enchantment> var0, EquipmentSlot var1) {
      return method_2(var0, var1, mc.player);
   }

   public static boolean method_9(ResourceKey<Enchantment> var0, ItemStack var1) {
      return method_2(var0, var1) > 0;
   }

   public static boolean method_9(ResourceKey<Enchantment> var0, EquipmentSlot var1, LivingEntity var2) {
      return method_2(var0, var1, var2) > 0;
   }

   public static boolean method_9(ResourceKey<Enchantment> var0, EquipmentSlot var1) {
      return method_2(var0, var1) > 0;
   }

   public static Map<ResourceKey<Enchantment>, Integer> method_7(ItemStack var0) {
      if (var0.isEmpty()) {
         return Collections.emptyMap();
      } else {
         HashMap var1 = new HashMap();

         for (Entry var3 : var0.getEnchantments().entrySet()) {
            Optional var4 = ((Holder)var3.getKey()).unwrapKey();
            if (!var4.isEmpty()) {
               var1.put((ResourceKey)var4.get(), var3.getIntValue());
            }
         }

         return var1;
      }
   }

   public static Holder<Enchantment> method_2(ResourceKey<Enchantment> var0) {
      return (Holder<Enchantment>)mc.level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(var0).orElse(null);
   }
}
