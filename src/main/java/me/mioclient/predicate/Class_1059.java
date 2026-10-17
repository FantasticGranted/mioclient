package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.SprintMode;
import me.mioclient.module.movement.SprintModule;

public class Class_1059 implements Predicate {
   public SprintModule sprint;

   public Class_1059(SprintModule var1) {
      super();
      this.sprint = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.sprint.mode.getValue() == SprintMode.INSTANT;
   }
}
