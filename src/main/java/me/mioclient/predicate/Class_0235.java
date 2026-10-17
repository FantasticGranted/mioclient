package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.enum_.TimerMode;
import me.mioclient.module.exploit.TimerModule;

public class Class_0235 implements Predicate {
   public TimerModule timer;

   public Class_0235(TimerModule var1) {
      super();
      this.timer = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.timer.mode.getValue() == TimerMode.DISABLE;
   }
}
