package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Criticals anticheat-bypass mode (packet / NCP / Grim / GrimV3). */
public enum CriticalsMode implements Nameable {
   PACKET("Packet"),
   NCP("NCP"),
   GRIM("Grim"),
   GRIMV3("2b2t");

   public final String displayName;

    CriticalsMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
