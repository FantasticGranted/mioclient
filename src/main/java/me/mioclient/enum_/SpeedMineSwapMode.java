package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** SpeedMine tool auto-swap mode: none, normal, or silent. */
public enum SpeedMineSwapMode implements Nameable {
   NONE("None"),
   NORMAL("Normal"),
   SILENT("Silent");

   public final String displayName;

    SpeedMineSwapMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
