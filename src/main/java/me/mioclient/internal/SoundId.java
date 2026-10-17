package me.mioclient.internal;

import net.minecraft.resources.Identifier;

// Originally extended Identifier; that class is final in MC 1.21.1, so we
// hold an Identifier instead and expose a `toIdentifier()` accessor.
/** Wraps a sound Identifier with its name and category. */
public final class SoundId {
   public final Identifier identifier;
   public final String name;
   public final String category;

   public SoundId(String var1, String var2) {
      this.identifier = Identifier.fromNamespaceAndPath(method_244(var1),
            new PlaceholderText().method_2((Object)var2).method_2((Object)var1).method_9("sounds//.ogg"));
      this.name = var2;
      this.category = var1;
   }

   public SoundId(String var1) {
      this("system", var1);
   }

   public String getName() {
      return this.name;
   }

   public String method_243() {
      return this.category;
   }

   public Identifier toIdentifier() {
      return this.identifier;
   }

   public static String method_244(String var0) {
      return var0.equalsIgnoreCase("system") ? "mio" : "mio-mount";
   }
}
