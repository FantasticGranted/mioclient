package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.ChamsModule;

public class Class_1217 implements Predicate {
   public ChamsModule chams;

   public Class_1217(ChamsModule var1) {
      super();
      this.chams = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.chams.speed.method_105();
   }
}
