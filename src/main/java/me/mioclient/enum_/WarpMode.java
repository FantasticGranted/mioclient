package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Warp mode: plain or alternative. */
public enum WarpMode implements Nameable {
   PLAIN("Plain"),
   ALTERNATIVE("Alternative");

   public final String displayName;

    WarpMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
