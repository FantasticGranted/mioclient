package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Hole ESP fill style: none, solid, or gradient. */
public enum HoleFillMode implements Nameable {
   NONE("None"),
   SOLID("Solid"),
   GRADIENT("Gradient");

   public final String displayName;

    HoleFillMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
