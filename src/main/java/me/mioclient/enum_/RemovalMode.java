package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** NoRender removal scope: partial or full. */
public enum RemovalMode implements Nameable {
   PARTIAL("Partial"),
   FULL("Full");

   public final String displayName;

    RemovalMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
