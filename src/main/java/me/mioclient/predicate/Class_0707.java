package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.SprintMode;
import me.mioclient.module.movement.SprintModule;

public class Class_0707 implements Predicate {
   public SprintModule sprint;

   public Class_0707(SprintModule var1) {
      super();
      this.sprint = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.sprint.mode.getValue() != SprintMode.INSTANT;
   }
}
