package me.mioclient.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import me.mioclient.internal.Class_1205;
import net.minecraft.client.Minecraft;

/** Base interface exposing shared singletons (mc client, Gson, method-handle lookups, thread pool) and small client-state helpers. */
public interface MinecraftAccess {
   Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
   Minecraft mc = Minecraft.getInstance();
   // The original referenced the deobfuscator-deleted `me.mioclient.w` central
   // dispatcher to acquire a Lookup for a non-public package. The stub keeps
   // event registration working without that crutch.
   Class_1205 lookups = new Class_1205() {
      {
         this.method_2("me.mioclient", (var0, var1) -> {
            try {
               return MethodHandles.lookup();
            } catch (Throwable t) {
               throw new RuntimeException(t);
            }
         });
      }
   };
   ExecutorService THREAD_POOL = Executors.newCachedThreadPool();

   default boolean method_535() {
      return mc.player == null || mc.level == null;
   }

   static boolean method_31(Object var0) {
      return mc.player == var0;
   }

   static boolean method_244() {
      try {
         Class.forName("me.mioclient.loader.Globals", false, MinecraftAccess.class.getClassLoader());
         return true;
      } catch (Exception var1) {
         return false;
      }
   }
}
