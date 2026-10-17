package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.HoleESPModule;

public class Class_0849 implements Predicate {
   public HoleESPModule holeESP;

   public Class_0849(HoleESPModule var1) {
      super();
      this.holeESP = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.holeESP.trapped.method_194();
   }
}
