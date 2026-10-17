package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.movement.EntityControlModule;

public class Class_0377 implements Predicate {
   public EntityControlModule entityControl;

   public Class_0377(EntityControlModule var1) {
      super();
      this.entityControl = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.entityControl.entitySpeed.method_194();
   }
}
