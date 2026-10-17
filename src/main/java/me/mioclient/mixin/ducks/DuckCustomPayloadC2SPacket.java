package me.mioclient.mixin.ducks;

import net.minecraft.network.protocol.common.ServerboundCustomPayloadPacket;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin({ServerboundCustomPayloadPacket.class})
public interface DuckCustomPayloadC2SPacket {
   @Mutable
   @Accessor("payload")
   void setCustomPayload(CustomPacketPayload var1);
}
