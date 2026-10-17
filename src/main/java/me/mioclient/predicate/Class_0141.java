package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.client.NotificationsModule;

public class Class_0141 implements Predicate {
   public NotificationsModule notifications;

   public Class_0141(NotificationsModule var1) {
      super();
      this.notifications = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.notifications.totemPops.method_194();
   }
}
