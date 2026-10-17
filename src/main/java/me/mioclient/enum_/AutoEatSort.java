package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import net.minecraft.world.item.consume_effects.ConsumeEffect;

/** AutoEat food sorting. */
public enum AutoEatSort implements Nameable {
   HUNGER("Hunger") {
      @Override
      public double method_2(ItemStack var1) {
         FoodProperties var2 = (FoodProperties)var1.get(DataComponents.FOOD);
         return var2 == null ? 0.0 : (double)var2.nutrition();
      }
   },
   SATURATION("Saturation") {
      @Override
      public double method_2(ItemStack var1) {
         FoodProperties var2 = (FoodProperties)var1.get(DataComponents.FOOD);
         return var2 == null ? 0.0 : (double)var2.saturation();
      }
   },
   HEALTH("Health") {
      @Override
      public double method_2(ItemStack var1) {
         Consumable var2 = (Consumable)var1.get(DataComponents.CONSUMABLE);
         if (var2 != null) {
            for (ConsumeEffect var3 : var2.onConsumeEffects()) {
               if (var3 instanceof ApplyStatusEffectsConsumeEffect var4) {
                  for (MobEffectInstance var5 : var4.effects()) {
                     if (var5.getEffect() == MobEffects.REGENERATION) {
                        return (double)var5.getAmplifier();
                     }
                  }
               }
            }
         }

         return 0.0;
      }
   };

   public final String displayName;

    AutoEatSort(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public abstract double method_2(ItemStack var1);
}
