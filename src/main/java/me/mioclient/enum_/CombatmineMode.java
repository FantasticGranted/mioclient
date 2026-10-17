package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Combatmine bypass logic: plain, Grim, or GrimV3. */
public enum CombatmineMode implements Nameable {
   PLAIN("Plain"),
   GRIM("Grim"),
   GRIMV3("NoVelocity");

   public final String displayName;

    CombatmineMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
