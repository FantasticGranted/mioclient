package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** ESP render mode: box, text, or both. */
public enum EspMode implements Nameable {
   BOX("Box"),
   TEXT("Text"),
   BOTH("Both");

   public final String displayName;

    EspMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
