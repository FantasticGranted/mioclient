package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import net.minecraft.resources.Identifier;

/** A selectable player cape with its id and texture (NONE = no cape). */
public enum Cape implements Nameable {
   NONE("none", null),
   MIO("mio", Identifier.fromNamespaceAndPath("mio", "capes/mio.png")),
   PEPSI("pepsi", Identifier.fromNamespaceAndPath("mio", "capes/pepsi.png")),
   NIGHTLY("nightly", Identifier.fromNamespaceAndPath("mio", "capes/nightly.png")),
   TETRIS("tetris", Identifier.fromNamespaceAndPath("mio", "capes/tetris.png"));

   public final String id;
   public final Identifier texture;

    Cape(String var3, Identifier var4) {
      this.id = var3;
      this.texture = var4;
   }

   @Override
   public String getName() {
      return this.id;
   }

   public Identifier getTexture() {
      return this.texture;
   }
}
