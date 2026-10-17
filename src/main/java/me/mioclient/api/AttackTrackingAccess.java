package me.mioclient.api;

/** Mixin accessor tracking an entity's spawn time and whether the client attacked it. */
public interface AttackTrackingAccess {
   long getSpawnTime();

   boolean isMioAttacked();

   void setMioAttacked(boolean var1);
}
