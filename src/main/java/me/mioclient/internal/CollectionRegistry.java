package me.mioclient.internal;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import me.mioclient.api.RegistryHolder;

/** Abstract RegistryHolder backed by a Collection, with predicate lookup over its entries. */
public abstract class CollectionRegistry<E, T extends Collection<E>> implements RegistryHolder<E, T> {
   public final T field_3243;

   public CollectionRegistry(T var1) {
      super();
      this.field_3243 = (T)var1;
   }

   public Optional<E> method_2(Predicate<E> var1) {
      for (E var3 : this.field_3243) {
         if (var1.test(var3)) {
            return Optional.of(var3);
         }
      }

      return Optional.empty();
   }

   public E method_9(Predicate<E> var1) {
      for (E var3 : this.field_3243) {
         if (var1.test(var3)) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public T getRegistry() {
      return this.field_3243;
   }
}
