package me.mioclient.internal;

import net.minecraft.client.StringSplitter;

/** TextHandler that measures character width via a custom font width provider. */
public class CustomTextHandler extends StringSplitter {
   public CustomTextHandler(MioGlyphRenderer var1) {
      super((var1x, var2) -> var1.method_7(Character.toString(var1x).charAt(0)));
   }
}
