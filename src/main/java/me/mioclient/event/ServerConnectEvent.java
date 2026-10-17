package me.mioclient.event;

import me.mioclient.internal.Event;

/** Fired when connecting to a server (carries the address). */
public class ServerConnectEvent extends Event {
   public final String field_266;

   public ServerConnectEvent(String var1) {
      super();
      this.field_266 = var1;
   }

   public String method_106() {
      return this.field_266;
   }
}
