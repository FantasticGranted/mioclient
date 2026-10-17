package me.mioclient.enum_;

import java.util.ArrayList;
import java.util.List;

/** Calendar season mapped to its months, with a month-to-season lookup. */
public enum Season {
   WINTER(1, 2, 12),
   SPRING(3, 4, 5),
   SUMMER(6, 7, 8),
   AUTUMN(9, 10, 11);

   public final List<Integer> months = new ArrayList<>();

    Season(int... var3) {
      for (int var7 : var3) {
         this.months.add(var7);
      }
   }

   public boolean method_29(int var1) {
      return this.months.contains(var1);
   }

   public static Season method_4(int var0) {
      for (Season var4 : values()) {
         if (var4.method_29(var0)) {
            return var4;
         }
      }

      throw new IllegalArgumentException("Unknown month");
   }
}
