package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Fullbright technique: screen shader, gamma, sky light, night-vision potion, or none. */
public enum FullbrightMode implements Nameable {
   SCREEN("Screen"),
   GAMMA("Gamma"),
   SKY("Sky"),
   POTION("Potion"),
   NONE("None");

   public final String displayName;

    FullbrightMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
