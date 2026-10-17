package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired when a chat message is received from the server. */
public class ChatReceiveEvent extends Event {
   public String field_483;

   public ChatReceiveEvent(String var1) {
      super();
      this.field_483 = var1;
   }

   public String method_219() {
      return this.field_483;
   }

   public void method_220(String var1) {
      this.field_483 = var1;
   }
}
