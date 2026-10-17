package me.mioclient.mixin.ducks;

import java.util.Optional;
import net.minecraft.network.protocol.game.ClientboundExplodePacket;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientboundExplodePacket.class})
public interface DuckExplosionS2CPacket {
   @Mutable
   @Accessor("playerKnockback")
   void setPlayerKnockback(Optional<Vec3> var1);
}
