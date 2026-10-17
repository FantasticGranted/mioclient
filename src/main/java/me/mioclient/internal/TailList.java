package me.mioclient.internal;

import java.util.ArrayList;

/** An ArrayList that can return its last element. */
public final class TailList<T> extends ArrayList<T> {
   public TailList() {
      super();
   }

   public T method_606() {
      return this.get(this.size() - 1);
   }
}
