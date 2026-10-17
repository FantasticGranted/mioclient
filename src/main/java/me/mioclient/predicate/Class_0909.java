package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.OffhandModule;

public class Class_0909 implements Predicate {
   public OffhandModule offhand;

   public Class_0909(OffhandModule var1) {
      super();
      this.offhand = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.offhand.gappleBind.method_194();
   }
}
