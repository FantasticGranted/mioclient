package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.ReplenishModule;

public class Class_0758 implements Predicate {
   public ReplenishModule replenish;

   public Class_0758(ReplenishModule var1) {
      super();
      this.replenish = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.replenish.limit.method_194();
   }
}
