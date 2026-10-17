package me.mioclient.api;

/** Something that can be flagged active/used, with a default activate helper. */
public interface Activatable {
   void method_52(boolean var1);

   default void method_463() {
      this.method_52(true);
   }

   boolean method_464();
}
