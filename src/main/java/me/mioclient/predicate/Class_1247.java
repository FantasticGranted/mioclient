package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.client.UIModule;

public class Class_1247 implements Predicate {
   public UIModule uI;

   public Class_1247(UIModule var1) {
      super();
      this.uI = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.uI.elements.method_194() && this.uI.windowShadow.method_194();
   }
}
