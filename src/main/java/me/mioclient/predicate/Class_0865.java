package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.LogoutSpotsModule;

public class Class_0865 implements Predicate {
   public LogoutSpotsModule logoutSpots;

   public Class_0865(LogoutSpotsModule var1) {
      super();
      this.logoutSpots = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.logoutSpots.nameTag.method_194();
   }
}
