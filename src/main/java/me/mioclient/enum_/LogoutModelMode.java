package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** LogoutSpots model rendering: none, simple, complex, or both. */
public enum LogoutModelMode implements Nameable {
   NONE("None"),
   SIMPLE("Simple"),
   COMPLEX("Complex"),
   BOTH("Both");

   public final String displayName;

    LogoutModelMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
