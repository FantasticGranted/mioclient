package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** FakeLag mode: blink or pulse. */
public enum FakeLagMode implements Nameable {
   BLINK("Blink"),
   PULSE("Pulse");

   public final String displayName;

    FakeLagMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
