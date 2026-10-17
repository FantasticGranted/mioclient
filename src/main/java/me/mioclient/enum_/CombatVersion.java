package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Which combat mechanics version to emulate: vanilla (1.9+), 1.8, or 1.12. */
public enum CombatVersion implements Nameable {
   VANILLA("Vanilla"),
   ONE_EIGHT("1.8"),
   ONE_TWELVE("1.12");

   public final String displayName;

    CombatVersion(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
