package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.SelfWebModule;

public class Class_1101 implements Predicate {
   public SelfWebModule selfWeb;

   public Class_1101(SelfWebModule var1) {
      super();
      this.selfWeb = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.selfWeb.smart.method_194();
   }
}
