package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.misc.AntiAFKModule;

public class Class_1090 implements Predicate {
   public AntiAFKModule antiAFK;

   public Class_1090(AntiAFKModule var1) {
      super();
      this.antiAFK = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.antiAFK.attack.getValue();
   }
}
