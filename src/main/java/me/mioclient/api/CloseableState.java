package me.mioclient.api;

/** Something with an open/closed state. */
public interface CloseableState {
   CloseableState field_3056 = () -> false;

   boolean isClosed();

   default boolean method_194() {
      return !this.isClosed();
   }
}
