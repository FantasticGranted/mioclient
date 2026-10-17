package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.HitmarkerModule;

public class Class_0318 implements Predicate {
   public HitmarkerModule hitmarker;

   public Class_0318(HitmarkerModule var1) {
      super();
      this.hitmarker = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.hitmarker.draw.method_194();
   }
}
