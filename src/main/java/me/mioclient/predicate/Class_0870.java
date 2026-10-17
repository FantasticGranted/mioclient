package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.AntiVoidMode;
import me.mioclient.module.movement.AntiVoidModule;

public class Class_0870 implements Predicate {
   public AntiVoidModule antiVoid;

   public Class_0870(AntiVoidModule var1) {
      super();
      this.antiVoid = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.antiVoid.mode.getValue() == AntiVoidMode.CANCEL;
   }
}
