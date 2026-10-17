package me.mioclient.api;

/** A one-argument function with access to shared client state. */
public interface ClientFunction<T, R> extends MinecraftAccess {
   R method_35(T var1);
}
