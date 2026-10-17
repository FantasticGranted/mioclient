package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.movement.FakeLagModule;

public class Class_0995 implements Predicate {
   public FakeLagModule fakeLag;

   public Class_0995(FakeLagModule var1) {
      super();
      this.fakeLag = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.fakeLag.render.method_194();
   }
}
