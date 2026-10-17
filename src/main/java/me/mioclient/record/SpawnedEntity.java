package me.mioclient.record;

import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;

/** Identifies a spawned entity by its network id and EntityType (built from an EntitySpawnS2CPacket). */
public final class SpawnedEntity {
   public final int entityId;
   public final EntityType<?> entityType;

   public SpawnedEntity(int var1, EntityType<?> var2) {
      super();
      this.entityId = var1;
      this.entityType = var2;
   }

   public static SpawnedEntity method_9(ClientboundAddEntityPacket var0) {
      return new SpawnedEntity(var0.getId(), var0.getType());
   }

   public int method_684() {
      return this.entityId;
   }

   public EntityType<?> getEntityType() {
      return this.entityType;
   }
}
