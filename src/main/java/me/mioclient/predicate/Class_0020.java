package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.internal.RotationManager;
import me.mioclient.module.movement.HoleSnapModule;

public class Class_0020 implements Predicate {
   public HoleSnapModule holeSnap;

   public Class_0020(HoleSnapModule var1) {
      super();
      this.holeSnap = var1;
   }

   @Override
   public boolean test(Object var1) {
      return !RotationManager.method_513();
   }
}
