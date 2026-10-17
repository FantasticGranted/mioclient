package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.LogoutModelMode;
import me.mioclient.module.render.LogoutSpotsModule;

public class Class_1358 implements Predicate {
   public LogoutSpotsModule logoutSpots;

   public Class_1358(LogoutSpotsModule var1) {
      super();
      this.logoutSpots = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.logoutSpots.model.getValue() == LogoutModelMode.SIMPLE || this.logoutSpots.model.getValue() == LogoutModelMode.BOTH;
   }
}
