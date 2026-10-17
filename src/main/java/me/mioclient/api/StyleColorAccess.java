package me.mioclient.api;

import java.util.function.Supplier;
import net.minecraft.network.chat.Style;

/** Mixin helper to derive a text Style from a color supplier. */
public interface StyleColorAccess {
   Style mio$withColor(Supplier<Integer> var1);
}
