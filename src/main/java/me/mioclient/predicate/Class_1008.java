package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.DeathTextMode;
import me.mioclient.module.misc.CustomDeathTextModule;

public class Class_1008 implements Predicate {
   public CustomDeathTextModule customDeathText;

   public Class_1008(CustomDeathTextModule var1) {
      super();
      this.customDeathText = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.customDeathText.mode.getValue() == DeathTextMode.CUSTOM;
   }
}
