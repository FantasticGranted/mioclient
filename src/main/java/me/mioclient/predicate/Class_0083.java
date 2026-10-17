package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.module.movement.ElytraFlyModule;

public class Class_0083 implements Predicate {
   public ElytraFlyModule elytraFly;

   public Class_0083(ElytraFlyModule var1) {
      super();
      this.elytraFly = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.elytraFly.mode.getValue() == ElytraFlyMode.BOOST && ElytraFlyModule.field_4348.method_1052();
   }
}
