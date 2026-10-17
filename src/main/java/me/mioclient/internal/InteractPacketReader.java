package me.mioclient.internal;

import me.mioclient.api.MinecraftAccess;
import net.minecraft.network.protocol.game.ServerboundInteractPacket;
import net.minecraft.world.entity.Entity;

/**
 * Reads the target entity back out of a serialized PlayerInteractEntityC2SPacket.
 */
public class InteractPacketReader implements MinecraftAccess {
   public InteractPacketReader() {
      super();
   }

   public static Entity method_2(ServerboundInteractPacket var0) {
      return mc.level.getEntity(var0.entityId());
   }
}
