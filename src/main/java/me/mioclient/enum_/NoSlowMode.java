package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** NoSlow bypass mode: none, NCP, Grim, GrimV3, or Constantiam. */
public enum NoSlowMode implements Nameable {
   NONE("None"),
   NCP("NCP"),
   GRIM("Grim"),
   GRIMV3("Slow"),
   CONSTANTIAM("Constantiam");

   public final String displayName;

    NoSlowMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
