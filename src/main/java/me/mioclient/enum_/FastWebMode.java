package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** FastWeb bypass mode (plain / strict / lagback / grim). */
public enum FastWebMode implements Nameable {
   PLAIN("Plain"),
   STRICT("Strict"),
   LAGBACK("LagBack"),
   GRIM("Grim");

   public final String displayName;

    FastWebMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
