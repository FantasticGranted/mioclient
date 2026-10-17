package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** FastLatency mode: old or alternative. */
public enum FastLatencyMode implements Nameable {
   OLD("Old"),
   ALTERNATIVE("Alternative");

   public final String displayName;

    FastLatencyMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
