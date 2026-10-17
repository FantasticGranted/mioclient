package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.Weather;
import me.mioclient.module.render.AmbienceModule;

public class Class_0560 implements Predicate {
   public AmbienceModule ambience;

   public Class_0560(AmbienceModule var1) {
      super();
      this.ambience = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.ambience.weather.getValue() == Weather.DUSTY;
   }
}
