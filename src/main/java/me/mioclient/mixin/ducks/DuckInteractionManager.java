package me.mioclient.mixin.ducks;

import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({MultiPlayerGameMode.class})
public interface DuckInteractionManager {
   @Invoker("ensureHasSentCarriedItem")
   void sync();
}
