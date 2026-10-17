package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.FastFallMode;
import me.mioclient.module.movement.FastFallModule;

public class Class_0466 implements Predicate {
   public FastFallModule fastFall;

   public Class_0466(FastFallModule var1) {
      super();
      this.fastFall = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.fastFall.mode.getValue() == FastFallMode.PLAIN;
   }
}
