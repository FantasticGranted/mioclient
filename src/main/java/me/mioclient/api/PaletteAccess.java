package me.mioclient.api;

import net.minecraft.util.BitStorage;
import net.minecraft.world.level.chunk.Palette;
import net.minecraft.world.level.chunk.PalettedContainer;

/** Mixin accessor exposing a PalettedContainer's storage and palette. */
public interface PaletteAccess<T> {
   BitStorage mio$storage();

   Palette<T> mio$palette();

   static BitStorage method_2(PalettedContainer<?> var0) {
      return ((DataHolder)var0).mio$data().mio$storage();
   }

   static <T> Palette<T> method_9(PalettedContainer<T> var0) {
      return ((DataHolder)var0).mio$data().mio$palette();
   }
}
