package me.mioclient.internal;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.ShulkerBoxBlock;

/** Item/block stack helpers (beds, shulker boxes, status effects). */
public final class ItemUtil {
   public ItemUtil() {
      super();
      throw new AssertionError();
   }

   public static boolean method_29(Item var0) {
      if (var0 instanceof BlockItem var1 && var1.getBlock() instanceof ShulkerBoxBlock) {
         return true;
      }

      return false;
   }

   public static boolean method_4(Item var0) {
      if (var0 instanceof BlockItem var1 && var1.getBlock() instanceof BedBlock) {
         return true;
      }

      return false;
   }

   public static boolean method_2(ItemStack var0, Holder<MobEffect> var1) {
      for (MobEffectInstance var3 : PotionUtil.method_78(var0).getEffects()) {
         if (var3.getEffect() == var1) {
            return true;
         }
      }

      return false;
   }
}
