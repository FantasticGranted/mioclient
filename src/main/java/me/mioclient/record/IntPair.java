package me.mioclient.record;

/** An immutable pair of ints with an EMPTY(-1,-1) sentinel. */
public final class IntPair {
   public final int first;
   public final int second;
   public static final IntPair EMPTY = new IntPair(-1, -1);

   public IntPair(int var1, int var2) {
      super();
      this.first = var1;
      this.second = var2;
   }

   public boolean method_644() {
      return this.first == -1 || this.second == -1;
   }

   public int getFirst() {
      return this.first;
   }

   public int getSecond() {
      return this.second;
   }
}
