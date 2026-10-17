package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.StepMode;
import me.mioclient.module.movement.StepModule;

public class Class_0157 implements Predicate {
   public StepModule step;

   public Class_0157(StepModule var1) {
      super();
      this.step = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.step.mode.getValue() == StepMode.NORMAL;
   }
}
