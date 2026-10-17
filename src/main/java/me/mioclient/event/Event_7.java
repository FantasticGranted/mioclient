package me.mioclient.event;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.Event;

public final class Event_7 extends Event implements MinecraftAccess {
   public Event_7() {
      super();
   }

   public float method_500() {
      return mc.player.getYRot();
   }

   public float method_501() {
      return mc.player.getXRot();
   }
}
