package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.movement.NoSlowModule;

public class Class_0569 implements Predicate {
   public NoSlowModule noSlow;

   public Class_0569(NoSlowModule var1) {
      super();
      this.noSlow = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.noSlow.blocks.method_194();
   }
}
