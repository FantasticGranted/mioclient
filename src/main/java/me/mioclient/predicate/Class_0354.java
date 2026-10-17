package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.AutoExpModule;

public class Class_0354 implements Predicate {
   public AutoExpModule autoExp;

   public Class_0354(AutoExpModule var1) {
      super();
      this.autoExp = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoExp.stop.method_194();
   }
}
