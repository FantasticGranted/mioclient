package me.mioclient.api;

/** Mixin accessor tracking whether an entity is server-side dead. */
public interface ServerSideDeadAccess {
   boolean isServerSideDead();

   void setServerSideDead(boolean var1);
}
