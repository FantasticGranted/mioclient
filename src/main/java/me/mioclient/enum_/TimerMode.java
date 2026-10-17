package me.mioclient.enum_;

import java.util.concurrent.TimeUnit;
import me.mioclient.api.Nameable;
import me.mioclient.module.exploit.TimerModule;

/** Timer mode. */
public enum TimerMode implements Nameable {
   NORMAL("Normal") {
      @Override
      public void method_2(TimerModule var1) {
         var1.field_1790 = var1.shift.getValue();
         var1.field_1789.method_2(var1, var1.shift.getValue());
      }
   },
   PULSE("Pulse") {
      @Override
      public void method_2(TimerModule var1) {
         boolean var2 = var1.field_1788
            .method_2((double)(var1.damaging ? var1.minDelay.getValue() : var1.maxDelay.getValue()).floatValue(), TimeUnit.SECONDS);
         var1.field_1790 = var1.damaging ? var1.pulseMax.getValue() : var1.pulseMin.getValue();
         var1.field_1789.method_2(var1, var1.damaging ? var1.pulseMin.getValue() : var1.pulseMax.getValue());
         if (var2) {
            var1.damaging = !var1.damaging;
            var1.field_1788.reset();
         }
      }
   },
   DISABLE("Disable") {
      @Override
      public void method_2(TimerModule var1) {
         var1.field_1790 = var1.shift.getValue();
         var1.field_1789.method_2(var1, var1.shift.getValue());
         if (var1.field_1788.method_2((double)var1.disableDelay.getValue().floatValue(), TimeUnit.SECONDS)) {
            var1.field_1788.reset();
            var1.disable();
         }
      }
   };

   public final String field_1208;

    TimerMode(String var3) {
      this.field_1208 = var3;
   }

   @Override
   public String getName() {
      return this.field_1208;
   }

   public abstract void method_2(TimerModule var1);
}
