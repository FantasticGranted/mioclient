package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.NoFallMode;
import me.mioclient.module.movement.NoFallModule;

public class Class_0122 implements Predicate {
   public NoFallModule noFall;

   public Class_0122(NoFallModule var1) {
      super();
      this.noFall = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.noFall.mode.getValue() == NoFallMode.TELEPORT || this.noFall.mode.getValue() == NoFallMode.MLG;
   }
}
