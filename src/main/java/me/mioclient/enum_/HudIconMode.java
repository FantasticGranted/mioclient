package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** How HUD icons behave: keep, move, or hide. */
public enum HudIconMode implements Nameable {
   KEEP("Keep"),
   MOVE("Move"),
   HIDE("Hide");

   public final String displayName;

    HudIconMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
