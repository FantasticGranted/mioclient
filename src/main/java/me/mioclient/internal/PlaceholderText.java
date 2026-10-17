package me.mioclient.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

/**
 * Deferred string formatter: collects argument strings, then substitutes each
 *  placeholder in a template with the next argument (used to build chat text).
 */
public final class PlaceholderText {
   public static final char PLACEHOLDER = 1;
   public static final char SEPARATOR = '\u0002';
   public static final String PLACEHOLDER_STR = String.valueOf(PLACEHOLDER);
   public final List<String> args = new ArrayList<>();

   public PlaceholderText() {
      super();
   }

   public PlaceholderText method_2(boolean var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(byte var1) {
      this.method_2(String.valueOf((int)var1));
      return this;
   }

   public PlaceholderText method_2(char var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(short var1) {
      this.method_2(String.valueOf((int)var1));
      return this;
   }

   public PlaceholderText method_2(int var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(long var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(double var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(float var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(boolean[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(byte[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(char[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(short[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(int[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(long[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(double[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(float[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(Object[] var1) {
      this.method_2(Arrays.toString(var1));
      return this;
   }

   public PlaceholderText method_2(Object var1) {
      this.method_2(String.valueOf(var1));
      return this;
   }

   public PlaceholderText method_2(String var1) {
      this.args.add(0, var1);
      return this;
   }

   public String method_9(String var1) {
      return method_2(var1, this.args);
   }

   public static String method_2(String var0, List<String> var1) {
      for (int var2 = 0; var0.indexOf(1) >= 0; var2++) {
         if (var2 == var1.size()) {
            return var0;
         }

         String var3 = (String)var1.get(var2);
         var0 = var0.replaceFirst(PLACEHOLDER_STR, Matcher.quoteReplacement(var3));
      }

      return var0;
   }
}
