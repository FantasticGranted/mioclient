package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.SpeedMineModule;

public class Class_0459 implements Predicate {
   public SpeedMineModule speedMine;

   public Class_0459(SpeedMineModule var1) {
      super();
      this.speedMine = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.speedMine.autoSwap.method_194();
   }
}
