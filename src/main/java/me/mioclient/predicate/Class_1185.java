package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.client.UIModule;

public class Class_1185 implements Predicate {
   public UIModule uI;

   public Class_1185(UIModule var1) {
      super();
      this.uI = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.uI.descriptions.method_194() && this.uI.delay.getValue() > 0;
   }
}
