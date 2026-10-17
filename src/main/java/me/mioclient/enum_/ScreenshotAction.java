package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** What to do after a screenshot: nothing, copy to clipboard, or upload to Imgur. */
public enum ScreenshotAction implements Nameable {
   NONE("None"),
   COPY("Copy"),
   IMGUR("Imgur");

   public final String displayName;

    ScreenshotAction(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
