package me.mioclient.api;

/** Holds typed data exposed via a mixin. */
public interface DataHolder<T> {
   PaletteAccess<T> mio$data();
}
