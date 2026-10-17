package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.HoleESPModule;

public class Class_1349 implements Predicate {
   public HoleESPModule holeESP;

   public Class_1349(HoleESPModule var1) {
      super();
      this.holeESP = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.holeESP.safe.method_194();
   }
}
