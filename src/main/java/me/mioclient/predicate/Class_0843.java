package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.abstract_.AbstractModule_39;

public class Class_0843 implements Predicate {
   public AbstractModule_39 module;

   public Class_0843(AbstractModule_39 var1) {
      super();
      this.module = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.module.render.method_194();
   }
}
