package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** NoInteract handling: cancel the interaction or shift-suppress it. */
public enum NoInteractMode implements Nameable {
   CANCEL("Cancel"),
   SHIFT("Shift");

   public final String displayName;

    NoInteractMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
