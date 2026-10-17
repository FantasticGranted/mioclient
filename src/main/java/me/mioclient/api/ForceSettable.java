package me.mioclient.api;

/** A value holder that can be force-set, bypassing normal validation. */
public interface ForceSettable<T> {
   void forceSetValue(T var1);
}
