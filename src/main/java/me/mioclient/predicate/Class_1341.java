package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.SearchModule;

public class Class_1341 implements Predicate {
   public SearchModule search;

   public Class_1341(SearchModule var1) {
      super();
      this.search = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.search.fill.getValue();
   }
}
