package me.mioclient.record;

/** An immutable pair of two String values. */
public final class StringPair {
   public final String first;
   public final String second;

   public StringPair(String var1, String var2) {
      super();
      this.first = var1;
      this.second = var2;
   }

   public String getFirst() {
      return this.first;
   }

   public String getSecond() {
      return this.second;
   }
}
