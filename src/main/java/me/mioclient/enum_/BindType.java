package me.mioclient.enum_;

import me.mioclient.api.Nameable;

/** How a keybind activates: toggle, hold, or reverse-hold, each with a display name and serialization id. */
public enum BindType implements Nameable {
   TOGGLE("Toggle", "toggle"),
   HOLD("Hold", "hold"),
   HOLD_REVERSE("ReverseHold", "hold_reverse");

   public final String displayName;
   public final String id;

    BindType(String var3, String var4) {
      this.displayName = var3;
      this.id = var4;
   }

   public String getId() {
      return this.id;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public static BindType method_4(String var0) {
      String var1 = var0.trim().toLowerCase();

      return switch (var1) {
         case "hold" -> HOLD;
         case "hold_reverse" -> HOLD_REVERSE;
         default -> TOGGLE;
      };
   }
}
