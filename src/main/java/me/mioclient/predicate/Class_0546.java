package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.RebreakMode;
import me.mioclient.module.player.SpeedMineModule;

public class Class_0546 implements Predicate {
   public SpeedMineModule speedMine;

   public Class_0546(SpeedMineModule var1) {
      super();
      this.speedMine = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.speedMine.rebreak2.getValue() == RebreakMode.INSTANT && this.speedMine.rebreak.method_194();
   }
}
