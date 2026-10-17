package me.mioclient.api;

/** Mixin hooks into the client player's movement/tick lifecycle. */
public interface PlayerTickHook {
   void sendMovementPacketsWrapper();

   void superTick();

   void resetEvent();

   void resetRotations();
}
