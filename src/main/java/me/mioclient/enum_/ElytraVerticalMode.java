package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Elytra-fly vertical control mode: none, plain, manual, or strict. */
public enum ElytraVerticalMode implements Nameable {
   NONE("None"),
   PLAIN("Plain"),
   MANUAL("Manual"),
   STRICT("Strict");

   public final String displayName;

    ElytraVerticalMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
