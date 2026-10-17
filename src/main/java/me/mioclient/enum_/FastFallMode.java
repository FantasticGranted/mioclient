package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** FastFall descent mode: plain, strict, or instant. */
public enum FastFallMode implements Nameable {
   PLAIN("Plain"),
   STRICT("Strict"),
   INSTANT("Instant");

   public final String displayName;

    FastFallMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
