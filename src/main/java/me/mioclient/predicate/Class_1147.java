package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.SpeedMineModule;

public class Class_1147 implements Predicate {
   public SpeedMineModule speedMine;

   public Class_1147(SpeedMineModule var1) {
      super();
      this.speedMine = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.speedMine.rebreak.method_194();
   }
}
