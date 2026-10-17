package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** LogoutSpots position info: none, coordinates, or distance. */
public enum LogoutPositionMode implements Nameable {
   NONE("None"),
   COORDINATES("Coordinates"),
   DISTANCE("Distance");

   public final String displayName;

    LogoutPositionMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
