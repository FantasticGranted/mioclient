package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired when a chat message/command is sent (carries the text). */
public class MessageSendEvent extends Event {
   public final String field_484;

   public MessageSendEvent(String var1) {
      super();
      this.field_484 = var1;
   }

   public String method_207() {
      return this.field_484;
   }
}
