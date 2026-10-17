package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoCrystal silent-swing mode: normal, alternative, or pick. */
public enum SilentMode implements Nameable {
   NORMAL("Normal"),
   ALTERNATIVE("Alternative"),
   PICK("Pick");

   public final String displayName;

    SilentMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
