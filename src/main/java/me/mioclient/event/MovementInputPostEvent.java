package me.mioclient.event;

import net.minecraft.client.player.ClientInput;

/** Fired after movement input is applied (post). */
public class MovementInputPostEvent extends MovementInputEvent {
   public MovementInputPostEvent(ClientInput var1, float var2) {
      super(var1, var2);
   }
}
