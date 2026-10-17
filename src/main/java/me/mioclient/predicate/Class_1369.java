package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.WaypointsModule;

public class Class_1369 implements Predicate {
   public WaypointsModule waypoints;

   public Class_1369(WaypointsModule var1) {
      super();
      this.waypoints = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.waypoints.name.getValue();
   }
}
