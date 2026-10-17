package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import me.mioclient.deobf.Named;
import me.mioclient.module.Module;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

/** Brigadier ArgumentType that parses a module by name, with suggestions. */
public class ModuleArgumentType implements ArgumentType<Module> {
   public ModuleArgumentType() {
      super();
   }

   public Module parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString();
      Optional var3 = Hub.field_2599.method_2(var1x -> {
         for (String var5 : var1x.getAliases()) {
            if (var5.equalsIgnoreCase(var2)) {
               return true;
            }
         }

         return false;
      });
      if (var3.isEmpty()) {
         throw new DynamicCommandExceptionType(var0 -> Component.nullToEmpty(new PlaceholderText().method_2(String.valueOf(var0)).method_9("Module not found \u0001")))
            .create(var2);
      } else {
         return (Module)var3.get();
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      return SharedSuggestionProvider.suggest(((List<Module>)Hub.field_2599.getRegistry()).stream().map(Module::getName), var2);
   }
}
