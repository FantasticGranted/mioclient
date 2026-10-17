package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.StrafeType;
import me.mioclient.module.movement.SpeedModule;

public class Class_1179 implements Predicate {
   public SpeedModule speed;

   public Class_1179(SpeedModule var1) {
      super();
      this.speed = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.speed.mode.getValue() != StrafeType.GRIM;
   }
}
