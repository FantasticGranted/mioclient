package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.BowAimModule;

public class Class_0257 implements Predicate {
   public BowAimModule bowAim;

   public Class_0257(BowAimModule var1) {
      super();
      this.bowAim = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.bowAim.targets.method_194();
   }
}
