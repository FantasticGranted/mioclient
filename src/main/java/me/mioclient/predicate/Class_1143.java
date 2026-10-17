package me.mioclient.predicate;

import java.util.function.Predicate;
import me.mioclient.module.combat.TriggerModule;

public class Class_1143 implements Predicate {
   public TriggerModule trigger;

   public Class_1143(TriggerModule var1) {
      super();
      this.trigger = var1;
   }

   @Override
   public boolean test(Object var1) {
      return this.trigger.field_3061.method_194() && this.trigger.field_3062.method_194();
   }
}
