package me.mioclient.mixin.ducks;

import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({LivingEntity.class})
public interface DuckLivingEntity {
   @Accessor("useItemRemaining")
   void setItemUseTimeLeft(int var1);

   @Accessor("attackStrengthTicker")
   int mio$getLastAttackedTicks();

   @Accessor("attackStrengthTicker")
   void mio$setLastAttackedTicks(int var1);

   @Accessor("swimAmount")
   void mio$setLeaningPitch(float var1);

   @Accessor("swimAmountO")
   void mio$setLastLeaningPitch(float var1);

   @Accessor("swimAmount")
   float mio$getLeaningPitch();

   @Accessor("swimAmount")
   float mio$getLastLeaningPitch();

   @Invoker("getCurrentSwingDuration")
   int getSwingDuration();
}
