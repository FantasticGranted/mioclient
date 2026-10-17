package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Elytra-fly movement mode (control / boost / packet / strict / bounce). */
public enum ElytraFlyMode implements Nameable {
   CONTROL("Control"),
   BOOST("Boost"),
   PACKET("Packet"),
   STRICT("Strict"),
   BOUNCE("Bounce");

   public final String displayName;

    ElytraFlyMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
