package me.mioclient.api;

import net.minecraft.network.chat.Component;

/** A type with a human-readable name; provides text conversion and an ad-hoc named factory. */
public interface Nameable {
   String getName();

   default Nameable method_14(String var1) {
      return () -> var1;
   }

   default Component method_15() {
      return Component.literal(this.getName());
   }

   static boolean method_16(Object var0) {
      return var0 instanceof Nameable;
   }
}
