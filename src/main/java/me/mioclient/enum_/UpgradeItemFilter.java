package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** AutoUpgrade item filter: any, tools, or armor. */
public enum UpgradeItemFilter implements Nameable {
   ANY("Any"),
   TOOLS("Tools"),
   ARMOR("Armor");

   public final String displayName;

    UpgradeItemFilter(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
