package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoCrystal auto-swap mode: none, normal, or silent. */
public enum AutoSwapMode implements Nameable {
   NONE("None"),
   NORMAL("Normal"),
   SILENT("Silent");

   public final String displayName;

    AutoSwapMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
