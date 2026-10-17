package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.WarpMode;
import me.mioclient.module.movement.WarpModule;

public class Class_0639 implements Predicate {
   public WarpModule warp;

   public Class_0639(WarpModule var1) {
      super();
      this.warp = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.warp.mode.getValue() == WarpMode.PLAIN;
   }
}
