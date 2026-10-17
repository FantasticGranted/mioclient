package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** Where AntiCyrillic applies: chat, commands, or both. */
public enum AntiCyrillicTarget implements Nameable {
   CHAT("Chat"),
   COMMANDS("Commands"),
   BOTH("Both");

   public final String displayName;

    AntiCyrillicTarget(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
