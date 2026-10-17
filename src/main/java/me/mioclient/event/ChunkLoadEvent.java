package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.world.level.chunk.LevelChunk;

/** Fired when a world chunk is loaded. */
public class ChunkLoadEvent extends Event {
   public final LevelChunk field_3541;

   public ChunkLoadEvent(LevelChunk var1) {
      super();
      this.field_3541 = var1;
   }

   public LevelChunk method_1021() {
      return this.field_3541;
   }
}
