package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.abstract_.AbstractModule_2;

public class Class_1118 implements Predicate {
   public AbstractModule_2 module;

   public Class_1118(AbstractModule_2 var1) {
      super();
      this.module = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.module.render.method_194();
   }
}
