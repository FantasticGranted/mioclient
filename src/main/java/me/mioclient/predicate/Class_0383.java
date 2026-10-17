package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.AutoLogModule;

public class Class_0383 implements Predicate {
   public AutoLogModule autoLog;

   public Class_0383(AutoLogModule var1) {
      super();
      this.autoLog = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.autoLog.totems.method_194();
   }
}
