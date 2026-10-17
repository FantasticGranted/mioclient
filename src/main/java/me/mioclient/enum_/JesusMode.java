package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Jesus (water-walk) mode: solid or dolphin. */
public enum JesusMode implements Nameable {
   SOLID("Solid"),
   DOLPHIN("Dolphin");

   public final String displayName;

    JesusMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
