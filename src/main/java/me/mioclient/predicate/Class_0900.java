package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.SpeedMineModule;

public class Class_0900 implements Predicate {
   public SpeedMineModule speedMine;

   public Class_0900(SpeedMineModule var1) {
      super();
      this.speedMine = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.speedMine.rotate.method_194();
   }
}
