package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.AutoFarmModule;

public class Class_1085 implements Predicate {
   public AutoFarmModule autoFarm;

   public Class_1085(AutoFarmModule var1) {
      super();
      this.autoFarm = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoFarm.crops.method_194();
   }
}
