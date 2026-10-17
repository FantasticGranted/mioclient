package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AntiVoid reaction when falling into the void: teleport or cancel. */
public enum AntiVoidMode implements Nameable {
   TELEPORT("Teleport"),
   CANCEL("Cancel");

   public final String displayName;

    AntiVoidMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
