package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.TotemType;
import me.mioclient.module.combat.OffhandModule;

public class Class_0324 implements Predicate {
   public OffhandModule offhand;

   public Class_0324(OffhandModule var1) {
      super();
      this.offhand = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.offhand.item.getValue() == TotemType.GAPPLE || this.offhand.gappleBind.method_194();
   }
}
