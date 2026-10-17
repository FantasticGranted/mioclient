package me.mioclient.api;

import java.util.Collection;

/** Holds a collection registry with register/unregister. */
public interface RegistryHolder<E, T extends Collection<E>> {
   T getRegistry();

   boolean register(E var1);

   boolean unregister(E var1);
}
