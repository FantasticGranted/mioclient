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
import me.mioclient.deobf.Named;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0874 implements ArgumentType<CommandBase> {
   public Class_0874() {
      super();
   }

   public CommandBase parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString();

      for (CommandBase var4 : (List<CommandBase>)Hub.field_2607.getRegistry()) {
         if (var4.getName().equalsIgnoreCase(var2)) {
            return var4;
         }
      }

      throw new DynamicCommandExceptionType(var0 -> Component.literal(String.format("Macro %s doesn't exists", var0))).create(var2);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      return SharedSuggestionProvider.suggest(((List<CommandBase>)Hub.field_2607.getRegistry()).stream().map(Named::getName), var2);
   }
}
