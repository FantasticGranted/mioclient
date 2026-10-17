package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.module.movement.ElytraFlyModule;

public class Class_0645 implements Predicate {
   public ElytraFlyModule elytraFly;

   public Class_0645(ElytraFlyModule var1) {
      super();
      this.elytraFly = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.elytraFly.mode.getValue() == ElytraFlyMode.PACKET || this.elytraFly.mode.getValue() == ElytraFlyMode.CONTROL;
   }
}
