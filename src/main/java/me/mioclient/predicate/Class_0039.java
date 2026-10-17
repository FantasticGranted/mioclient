package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.RotationMode;
import me.mioclient.module.abstract_.AbstractModule_36;

public class Class_0039 implements Predicate {
   public AbstractModule_36 module;

   public Class_0039(AbstractModule_36 var1) {
      super();
      this.module = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.module.rotations.getValue() == RotationMode.MOTION;
   }
}
