package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.JesusMode;
import me.mioclient.module.movement.JesusModule;

public class Class_1076 implements Predicate {
   public JesusModule jesus;

   public Class_1076(JesusModule var1) {
      super();
      this.jesus = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.jesus.mode.getValue() == JesusMode.SOLID;
   }
}
