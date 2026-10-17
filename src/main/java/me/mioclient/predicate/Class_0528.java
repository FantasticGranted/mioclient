package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.FullbrightMode;
import me.mioclient.module.render.AmbienceModule;

public class Class_0528 implements Predicate {
   public AmbienceModule ambience;

   public Class_0528(AmbienceModule var1) {
      super();
      this.ambience = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.ambience.brightness.getValue() == FullbrightMode.SKY;
   }
}
