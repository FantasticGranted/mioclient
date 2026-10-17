package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.client.UIModule;

public class Class_0270 implements Predicate {
   public UIModule uI;

   public Class_0270(UIModule var1) {
      super();
      this.uI = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.uI.colors.method_194();
   }
}
