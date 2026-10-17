package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Phase (noclip) technique: pearl or clip. */
public enum PhaseMode implements Nameable {
   PEARL("Pearl"),
   CLIP("Clip");

   public final String displayName;

    PhaseMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
