package me.mioclient.api;

import me.mioclient.enum_.RelationType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

/** Mixin accessor for per-entity mod state (eating time, relation role, wall proximity). */
public interface EntityInfoAccess {
   long mio$getLastEatingTime();

   RelationType mio$getRole();

   void mio$setRole(RelationType var1);

   boolean mio$isNextToWall();

   default float method_2(ItemStack var1, LivingEntity var2) {
      return (float)(System.currentTimeMillis() - this.mio$getLastEatingTime()) / ((float)var1.getUseDuration(var2) * 50.0F + 50.0F);
   }
}
