package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.FullbrightMode;
import me.mioclient.module.render.AmbienceModule;

public class Class_0090 implements Predicate {
   public AmbienceModule ambience;

   public Class_0090(AmbienceModule var1) {
      super();
      this.ambience = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.ambience.brightness.getValue() == FullbrightMode.SCREEN;
   }
}
