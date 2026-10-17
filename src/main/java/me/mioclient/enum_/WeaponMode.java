package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Aura weapon requirement: none, require, or swap. */
public enum WeaponMode implements Nameable {
   NONE("None"),
   REQUIRE("Require"),
   SWAP("Swap");

   public final String displayName;

    WeaponMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
