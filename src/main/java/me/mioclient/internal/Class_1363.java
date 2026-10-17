package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_1363 implements ArgumentType<ItemMatcherProfile> {
   public Class_1363() {
      super();
   }

   public ItemMatcherProfile parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString();

      for (ItemMatcherProfile var4 : (List<ItemMatcherProfile>)Hub.field_2625.getRegistry()) {
         if (var4.getName().equalsIgnoreCase(var2)) {
            return var4;
         }
      }

      throw new DynamicCommandExceptionType(var0 -> Component.literal(String.format("Kit %s doesn't exist", var0))).create(var2);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      return SharedSuggestionProvider.suggest(((List<ItemMatcherProfile>)Hub.field_2625.getRegistry()).stream().map(ItemMatcherProfile::getName), var2);
   }
}
