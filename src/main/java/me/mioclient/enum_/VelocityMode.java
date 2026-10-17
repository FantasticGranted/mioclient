package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Velocity (knockback) handling mode: plain, grim, or walls. */
public enum VelocityMode implements Nameable {
   PLAIN("Plain"),
   GRIM("Grim"),
   WALLS("Walls");

   public final String displayName;

    VelocityMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
