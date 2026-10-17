package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.LogoutSpotsModule;

public class Class_0762 implements Predicate {
   public LogoutSpotsModule logoutSpots;

   public Class_0762(LogoutSpotsModule var1) {
      super();
      this.logoutSpots = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.logoutSpots.nameTag.method_194();
   }
}
