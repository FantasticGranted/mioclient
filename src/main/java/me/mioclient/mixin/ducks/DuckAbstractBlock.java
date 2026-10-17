package me.mioclient.mixin.ducks;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({BlockBehaviour.class})
public interface DuckAbstractBlock {
   @Accessor("hasCollision")
   boolean isCollidable();
}
