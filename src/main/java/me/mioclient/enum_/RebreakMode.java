package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** SpeedMine rebreak mode: none, fast, or instant. */
public enum RebreakMode implements Nameable {
   NONE("None"),
   FAST("Fast"),
   INSTANT("Instant");

   public final String displayName;

    RebreakMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
