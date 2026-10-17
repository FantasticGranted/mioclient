package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.NoSlowMode;
import me.mioclient.module.movement.NoSlowModule;

public class Class_0389 implements Predicate {
   public NoSlowModule noSlow;

   public Class_0389(NoSlowModule var1) {
      super();
      this.noSlow = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.noSlow.mode.getValue() != NoSlowMode.GRIM && this.noSlow.mode.getValue() != NoSlowMode.GRIMV3;
   }
}
