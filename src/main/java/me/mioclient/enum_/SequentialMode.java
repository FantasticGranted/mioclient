package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoCrystal sequential mode: none, strict, or strong. */
public enum SequentialMode implements Nameable {
   NONE("None"),
   STRICT("Strict"),
   STRONG("Strong");

   public final String displayName;

    SequentialMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public boolean method_1214() {
      return this != NONE;
   }
}
