package me.mioclient.mixin.ducks;

import net.minecraft.network.protocol.game.ServerboundChatPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ServerboundChatPacket.class})
public interface DuckChatMessageC2SPacket {
   @Mutable
   @Accessor("message")
   void setChatMessage(String var1);
}
