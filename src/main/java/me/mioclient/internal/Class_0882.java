package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import me.mioclient.record.StringPair;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0882 implements ArgumentType<StringPair> {
   public Class_0882() {
      super();
   }

   public StringPair parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString();
      Optional var3 = Hub.field_2627.method_310(var2);
      if (var3.isEmpty()) {
         throw new DynamicCommandExceptionType(var0 -> Component.nullToEmpty(new PlaceholderText().method_2(String.valueOf(var0)).method_9("Filter not found \u0001")))
            .create(var2);
      } else {
         return (StringPair)var3.get();
      }
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      return SharedSuggestionProvider.suggest(Hub.field_2627.method_335(), var2);
   }
}
