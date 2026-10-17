package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.player.ScaffoldModule;

public class Class_0063 implements Predicate {
   public ScaffoldModule scaffold;

   public Class_0063(ScaffoldModule var1) {
      super();
      this.scaffold = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.scaffold.render.method_194();
   }
}
