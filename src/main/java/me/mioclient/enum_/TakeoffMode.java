package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Elytra takeoff mode: none, plain, or strict. */
public enum TakeoffMode implements Nameable {
   NONE("None"),
   PLAIN("Plain"),
   STRICT("Strict");

   public final String displayName;

    TakeoffMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
