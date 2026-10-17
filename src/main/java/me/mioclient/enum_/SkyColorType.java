package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Sky color type: none, flat, or end. */
public enum SkyColorType implements Nameable {
   NONE("None"),
   FLAT("Flat"),
   END("End");

   public final String displayName;

    SkyColorType(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
