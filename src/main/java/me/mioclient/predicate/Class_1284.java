package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.TrailsModule;

public class Class_1284 implements Predicate {
   public TrailsModule trails;

   public Class_1284(TrailsModule var1) {
      super();
      this.trails = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.trails.targets.method_194();
   }
}
