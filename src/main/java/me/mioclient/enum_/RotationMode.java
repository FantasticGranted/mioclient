package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** How rotations are sent: packet, client, or motion. */
public enum RotationMode implements Nameable {
   PACKET("Packet"),
   CLIENT("Client"),
   MOTION("Motion");

   public final String displayName;

    RotationMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
