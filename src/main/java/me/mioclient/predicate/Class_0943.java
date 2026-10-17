package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.TrailsModule;

public class Class_0943 implements Predicate {
   public TrailsModule trails;

   public Class_0943(TrailsModule var1) {
      super();
      this.trails = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.trails.targets.method_194();
   }
}
