package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.abstract_.AbstractModule_2;

public class Class_0182 implements Predicate {
   public AbstractModule_2 module;

   public Class_0182(AbstractModule_2 var1) {
      super();
      this.module = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.module.render.method_194();
   }
}
