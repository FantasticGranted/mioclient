package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Chest interaction mode: steal, fill, drop, refill, or rekit. */
public enum ChestStealMode implements Nameable {
   STEAL("Steal"),
   FILL("Fill"),
   DROP("Drop"),
   REFILL("Refill"),
   REKIT("Rekit");

   public final String displayName;

    ChestStealMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
