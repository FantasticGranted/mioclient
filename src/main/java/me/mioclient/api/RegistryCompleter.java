package me.mioclient.api;

import java.util.Collection;
import java.util.List;
import me.mioclient.internal.Class_0391;
import me.mioclient.internal.Class_0779;
import me.mioclient.internal.Class_1098;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;

/** Provides autocomplete suggestions for a Minecraft registry's entries. */
public interface RegistryCompleter {
   List<RegistryCompleter> field_279 = List.of(new Class_1098(), new Class_0391());
   Class_0779 field_280 = new Class_0779();

   Collection<String> method_114(String var1);

   Collection<String> method_115();

   Identifier method_116();

   static RegistryCompleter method_2(Registry<?> var0) {
      for (RegistryCompleter var2 : field_279) {
         if (var0.key().identifier().equals(var2.method_116())) {
            return var2;
         }
      }

      return field_280;
   }
}
