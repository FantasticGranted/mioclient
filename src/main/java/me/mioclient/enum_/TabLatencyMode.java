package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** ExtraTab latency display: vanilla, text, or none. */
public enum TabLatencyMode implements Nameable {
   VANILLA("Vanilla"),
   TEXT("Text"),
   NONE("None");

   public final String displayName;

    TabLatencyMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
