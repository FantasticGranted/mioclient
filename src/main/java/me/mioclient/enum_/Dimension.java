package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** The three Minecraft dimensions with their registry id and display name. */
public enum Dimension implements Nameable {
   OVERWORLD("overworld", "Overworld"),
   THE_NETHER("the_nether", "Nether"),
   THE_END("the_end", "End");

   public final String id;
   public final String displayName;

    Dimension(String var3, String var4) {
      this.id = var3;
      this.displayName = var4;
   }

   public boolean isOverworld() {
      return this == OVERWORLD;
   }

   public boolean isNether() {
      return this == THE_NETHER;
   }

   public boolean isEnd() {
      return this == THE_END;
   }

   public String getDisplayName() {
      return this.displayName;
   }

   @Override
   public String getName() {
      return this.id;
   }
}
