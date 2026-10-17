package me.mioclient.record;

/** Immutable generic pair holding a left and right value. */
public final class Pair<L, R> {
   public final L left;
   public final R right;

   public Pair(L var1, R var2) {
      super();
      this.left = (L)var1;
      this.right = (R)var2;
   }

   public L getLeft() {
      return this.left;
   }

   public R getRight() {
      return this.right;
   }
}
