package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.AntiPhaseModule;

public class Class_0489 implements Predicate {
   public AntiPhaseModule antiPhase;

   public Class_0489(AntiPhaseModule var1) {
      super();
      this.antiPhase = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.antiPhase.render.method_194();
   }
}
