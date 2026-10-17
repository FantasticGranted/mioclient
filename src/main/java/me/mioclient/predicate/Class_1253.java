package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.render.VoidESPModule;

public class Class_1253 implements Predicate {
   public VoidESPModule voidESP;

   public Class_1253(VoidESPModule var1) {
      super();
      this.voidESP = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.voidESP.fade.getValue();
   }
}
