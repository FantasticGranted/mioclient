package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.misc.ExtraTabModule;

public class Class_1343 implements Predicate {
   public ExtraTabModule extraTab;

   public Class_1343(ExtraTabModule var1) {
      super();
      this.extraTab = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.extraTab.highlight.method_194();
   }
}
