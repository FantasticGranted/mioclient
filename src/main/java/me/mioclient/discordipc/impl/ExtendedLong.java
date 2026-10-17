package me.mioclient.discordipc.impl;

public class ExtendedLong {
   public ExtendedLong() {
      super();
   }

   public static int hashCode(long var0) {
      return (int)(var0 ^ var0 >>> 32);
   }
}
