package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.PhaseMode;
import me.mioclient.module.exploit.PhaseModule;

public class Class_0902 implements Predicate {
   public PhaseModule phase;

   public Class_0902(PhaseModule var1) {
      super();
      this.phase = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.phase.mode.getValue() == PhaseMode.PEARL;
   }
}
