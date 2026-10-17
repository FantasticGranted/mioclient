package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.HitmarkerModule;

public class Class_0545 implements Predicate {
   public HitmarkerModule hitmarker;

   public Class_0545(HitmarkerModule var1) {
      super();
      this.hitmarker = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.hitmarker.targets.method_194();
   }
}
