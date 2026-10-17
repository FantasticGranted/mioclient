package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AntiAim look behaviour: avoid or stare. */
public enum AntiAimLook implements Nameable {
   AVOID("Avoid"),
   STARE("Stare");

   public final String displayName;

    AntiAimLook(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
