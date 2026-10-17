package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** NameTags visibility filter: show, only, or hide. */
public enum NameTagVisibility implements Nameable {
   SHOW("Show"),
   ONLY("Only"),
   HIDE("Hide");

   public final String displayName;

    NameTagVisibility(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
