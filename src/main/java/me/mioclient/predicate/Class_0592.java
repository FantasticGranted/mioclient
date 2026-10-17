package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.AmbienceModule;

public class Class_0592 implements Predicate {
   public AmbienceModule ambience;

   public Class_0592(AmbienceModule var1) {
      super();
      this.ambience = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.ambience.worldTime.method_194() && !this.ambience.sync.getValue();
   }
}
