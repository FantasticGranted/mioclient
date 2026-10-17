package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoCrystal instant-place mode: always, strict, or none. */
public enum InstantMode implements Nameable {
   ALWAYS("Always"),
   STRICT("Strict"),
   NONE("None");

   public final String displayName;

    InstantMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
