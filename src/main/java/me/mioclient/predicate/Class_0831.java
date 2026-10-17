package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.client.NotificationsModule;

public class Class_0831 implements Predicate {
   public NotificationsModule notifications;

   public Class_0831(NotificationsModule var1) {
      super();
      this.notifications = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.notifications.totemPops.method_194() && this.notifications.sayChat.method_194();
   }
}
