package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Item-swap mode: normal, swap-back, or silent. */
public enum SwapMode implements Nameable {
   NORMAL("Normal"),
   SWAPBACK("SwapBack"),
   SILENT("Silent");

   public final String displayName;

    SwapMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
