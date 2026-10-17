package me.mioclient.internal;

import java.util.Collection;
import java.util.Collections;
import me.mioclient.api.RegistryCompleter;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;

public class Class_0391 implements RegistryCompleter {
   public Class_0391() {
      super();
   }

   @Override
   public Collection<String> method_114(String var1) {
      Collection var2 = RegistryCompleter.method_2(BuiltInRegistries.BLOCK).method_114(var1);
      return (Collection<String>)(!var2.isEmpty() ? var2 : Collections.emptyList());
   }

   @Override
   public Collection<String> method_115() {
      return RegistryCompleter.method_2(BuiltInRegistries.BLOCK).method_115();
   }

   @Override
   public Identifier method_116() {
      return Registries.ITEM.identifier();
   }
}
