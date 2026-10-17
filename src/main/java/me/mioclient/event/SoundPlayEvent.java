package me.mioclient.event;

import me.mioclient.internal.Event;
import net.minecraft.client.resources.sounds.SoundInstance;

/** Fired when a sound instance is about to play. */
public final class SoundPlayEvent extends Event {
   public final SoundInstance field_2952;

   public SoundPlayEvent(SoundInstance var1) {
      super();
      this.field_2952 = var1;
   }

   public SoundInstance method_863() {
      return this.field_2952;
   }
}
