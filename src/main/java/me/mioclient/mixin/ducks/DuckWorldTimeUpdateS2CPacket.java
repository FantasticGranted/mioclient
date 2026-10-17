package me.mioclient.mixin.ducks;

import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ClientboundSetTimePacket.class})
public interface DuckWorldTimeUpdateS2CPacket {
   @Mutable
   @Accessor("gameTime")
   void setTime(long var1);
}
