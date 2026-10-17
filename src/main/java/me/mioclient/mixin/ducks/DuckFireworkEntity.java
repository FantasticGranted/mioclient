package me.mioclient.mixin.ducks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({FireworkRocketEntity.class})
public interface DuckFireworkEntity {
   @Accessor("life")
   void setLife(int var1);

   @Accessor("life")
   int getLife();

   @Accessor("lifetime")
   int getLifeTime();

   @Accessor("attachedToEntity")
   LivingEntity mio$getShooter();
}
