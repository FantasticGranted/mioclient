package me.mioclient.internal;

import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionContents;

/** Helpers for reading potion/status-effect data from item stacks. */
public class PotionUtil {
   public static final Potion field_1158 = new Potion("empty", new MobEffectInstance[0]);

   public PotionUtil() {
      super();
   }

   public static Potion method_78(ItemStack var0) {
      PotionContents var1 = (PotionContents)var0.get(DataComponents.POTION_CONTENTS);
      if (var1 == null) {
         return field_1158;
      } else {
         Holder var2 = (Holder)var1.potion().orElse(null);
         return var2 == null ? field_1158 : (Potion)var2.value();
      }
   }
}
