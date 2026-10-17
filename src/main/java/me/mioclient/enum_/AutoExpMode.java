package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoExp throwing mode: normal or silent. */
public enum AutoExpMode implements Nameable {
   NORMAL("Normal"),
   SILENT("Silent");

   public final String displayName;

    AutoExpMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
