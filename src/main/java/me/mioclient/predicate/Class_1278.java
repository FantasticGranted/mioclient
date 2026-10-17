package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.VelocityMode;
import me.mioclient.module.movement.VelocityModule;

public class Class_1278 implements Predicate {
   public VelocityModule velocity;

   public Class_1278(VelocityModule var1) {
      super();
      this.velocity = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.velocity.mode.getValue() == VelocityMode.PLAIN;
   }
}
