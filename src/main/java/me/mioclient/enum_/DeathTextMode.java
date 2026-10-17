package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** CustomDeathText source: custom or random. */
public enum DeathTextMode implements Nameable {
   CUSTOM("Custom"),
   RANDOM("Random");

   public final String displayName;

    DeathTextMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
