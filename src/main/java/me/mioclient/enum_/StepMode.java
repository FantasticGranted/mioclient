package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Step mode: vanilla or normal. */
public enum StepMode implements Nameable {
   VANILLA("Vanilla"),
   NORMAL("Normal");

   public final String displayName;

    StepMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
