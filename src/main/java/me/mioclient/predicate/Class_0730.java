package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.CrosshairModule;

public class Class_0730 implements Predicate {
   public CrosshairModule crosshair;

   public Class_0730(CrosshairModule var1) {
      super();
      this.crosshair = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.crosshair.dynamic.method_194();
   }
}
