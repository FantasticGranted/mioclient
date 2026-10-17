package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.AutoTameModule;

public class Class_0759 implements Predicate {
   public AutoTameModule autoTame;

   public Class_0759(AutoTameModule var1) {
      super();
      this.autoTame = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoTame.targets.method_194();
   }
}
