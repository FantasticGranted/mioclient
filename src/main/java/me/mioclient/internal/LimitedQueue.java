package me.mioclient.internal;

import java.util.LinkedList;

/** A size-capped, de-duplicating linked list; oldest entries drop when full. */
public final class LimitedQueue<E> extends LinkedList<E> {
   public final int field_1746;

   @Override
   public boolean add(E var1) {
      if (this.size() >= this.field_1746) {
         this.removeFirst();
      }

      return this.contains(var1) ? false : super.add((E)var1);
   }

   public LimitedQueue(int var1) {
      super();
      this.field_1746 = var1;
   }
}
