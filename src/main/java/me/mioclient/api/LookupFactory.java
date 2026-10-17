package me.mioclient.api;

import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;

/** Creates a MethodHandles.Lookup for a given method and target class. */
public interface LookupFactory {
   Lookup create(Method var1, Class<?> var2);
}
