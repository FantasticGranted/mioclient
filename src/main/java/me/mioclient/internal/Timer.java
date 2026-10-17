package me.mioclient.internal;

import java.util.concurrent.TimeUnit;

/**
 * Simple reset-based timer/cooldown: remembers the last reset time and reports whether a
 * given duration (in any TimeUnit) has elapsed, optionally resetting on success.
 */
public class Timer {
   public long field_677 = -1L;

   public Timer() {
      super();
   }

   public boolean method_2(double var1, TimeUnit var3) {
      return this.method_9(method_5(var1, var3));
   }

   public boolean method_9(long var1) {
      return System.currentTimeMillis() - this.field_677 >= var1;
   }

   public boolean method_5(long var1) {
      boolean var3 = this.method_9(var1);
      if (var3) {
         this.reset();
      }

      return var3;
   }

   public boolean method_2(long var1, TimeUnit var3) {
      return this.method_5(method_5((double)var1, var3));
   }

   public long method_271() {
      return System.currentTimeMillis() - this.field_677;
   }

   public void reset() {
      this.field_677 = System.currentTimeMillis();
   }

   public void setTime(long var1) {
      this.field_677 = var1;
   }

   public void method_9(double var1, TimeUnit var3) {
      this.field_677 = method_5(var1, var3);
   }

   public static long method_5(double var0, TimeUnit var2) {
      return switch (var2) {
         case NANOSECONDS -> (long)(var0 * 0.001 * 0.001);
         case MICROSECONDS -> (long)(var0 * 0.001);
         case MILLISECONDS -> (long)var0;
         case SECONDS -> (long)(var0 * 1e+03);
         case MINUTES -> (long)(var0 * 1e+03 * 6e+01);
         case HOURS -> (long)(
         var0 * 1e+03 * 6e+01 * 6e+01
      );
         case DAYS -> (long)(
         var0
            * 1e+03
            * 6e+01
            * 6e+01
            * 24.0
      );
      };
   }
}
