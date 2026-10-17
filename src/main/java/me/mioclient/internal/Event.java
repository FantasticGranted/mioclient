package me.mioclient.internal;

/** Base class for all mod events; can be cancelled and reset. */
public class Event {
   public boolean cancelled;

   public Event() {
      super();
   }

   public void method_463() {
      this.cancelled = true;
   }

   public void method_616() {
      this.cancelled = false;
   }

   public boolean method_464() {
      return this.cancelled;
   }
}
