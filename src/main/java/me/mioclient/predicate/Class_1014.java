package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.ElytraVerticalMode;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.module.movement.ElytraFlyModule;

public class Class_1014 implements Predicate {
   public ElytraFlyModule elytraFly;

   public Class_1014(ElytraFlyModule var1) {
      super();
      this.elytraFly = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.elytraFly.vertical.getValue() == ElytraVerticalMode.PLAIN && this.elytraFly.mode.getValue() == ElytraFlyMode.CONTROL;
   }
}
