package me.mioclient.mixin.ducks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({Entity.class})
public interface DuckEntity {
   @Invoker("getBlockPosBelowThatAffectsMyMovement")
   BlockPos mio$getVelocityAffectingPos();
}
