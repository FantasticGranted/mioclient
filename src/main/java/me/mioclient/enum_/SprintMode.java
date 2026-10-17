package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Sprint activation mode: legit, rage, or instant. */
public enum SprintMode implements Nameable {
   LEGIT("Legit"),
   RAGE("Rage"),
   INSTANT("Instant");

   public final String displayName;

    SprintMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
