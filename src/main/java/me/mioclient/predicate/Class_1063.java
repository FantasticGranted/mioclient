package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.movement.FlightModule;

public class Class_1063 implements Predicate {
   public FlightModule flight;

   public Class_1063(FlightModule var1) {
      super();
      this.flight = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.flight.vertical.method_194();
   }
}
