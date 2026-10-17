package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.WaypointsModule;

public class Class_1304 implements Predicate {
   public WaypointsModule waypoints;

   public Class_1304(WaypointsModule var1) {
      super();
      this.waypoints = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.waypoints.name.getValue();
   }
}
