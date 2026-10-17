package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** ChestStealer search target: by name or by item. */
public enum ChestSearchMode implements Nameable {
   NAME("Name"),
   ITEM("Item");

   public final String displayName;

    ChestSearchMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
