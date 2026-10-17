package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired on a keyboard key action. */
public final class KeyPressEvent extends Event {
   public final int field_4205;

   public KeyPressEvent(int var1) {
      super();
      this.field_4205 = var1;
   }

   public String method_1166() {
      return Character.toString(this.field_4205);
   }

   public char method_1167() {
      return this.method_1166().charAt(0);
   }
}
