package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.Class_0174;
import me.mioclient.module.player.SpeedMineModule;

public class Class_0938 implements Predicate {
   public SpeedMineModule speedMine;

   public Class_0938(SpeedMineModule var1) {
      super();
      this.speedMine = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.speedMine.render.method_194() && this.speedMine.field_4012.getValue() == Class_0174.CUSTOM;
   }
}
