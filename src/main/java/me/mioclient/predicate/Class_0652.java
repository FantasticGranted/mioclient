package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.AutoBreedModule;

public class Class_0652 implements Predicate {
   public AutoBreedModule autoBreed;

   public Class_0652(AutoBreedModule var1) {
      super();
      this.autoBreed = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoBreed.targets.method_194();
   }
}
