package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.module.player.ChestStealerModule;

public class Class_0133 implements Predicate {
   public ChestStealerModule chestStealer;

   public Class_0133(ChestStealerModule var1) {
      super();
      this.chestStealer = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.chestStealer.select.getValue() != SelfFillMode.ANY;
   }
}
