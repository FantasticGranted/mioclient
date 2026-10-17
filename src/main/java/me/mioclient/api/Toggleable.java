package me.mioclient.api;

/** Something with a toggled state that can be enabled/disabled. */
public interface Toggleable {
   boolean isToggled();

   default void method_38(boolean var1) {
      if (this.isToggled() != var1) {
         this.method_68();
      }
   }

   default void method_68() {
      if (this.isToggled()) {
         this.disable();
      } else {
         this.enable();
      }
   }

   void enable();

   void disable();
}
