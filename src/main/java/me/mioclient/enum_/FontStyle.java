package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Font styling option: plain, bold, italic, or bold-italic. */
public enum FontStyle implements Nameable {
   PLAIN("Plain"),
   BOLD("Bold"),
   ITALIC("Italic"),
   BOLDITALIC("BoldItalic");

   public final String displayName;

    FontStyle(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
