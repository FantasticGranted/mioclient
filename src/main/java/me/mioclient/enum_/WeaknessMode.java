package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoCrystal weakness-swap mode: none, normal, or silent. */
public enum WeaknessMode implements Nameable {
   NONE("None"),
   NORMAL("Normal"),
   SILENT("Silent");

   public final String displayName;

    WeaknessMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
