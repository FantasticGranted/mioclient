package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0119 implements ArgumentType<NamedPosition> {
   public Class_0119() {
      super();
   }

   public NamedPosition parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString();

      for (NamedPosition var4 : (List<NamedPosition>)Hub.field_2604.getRegistry()) {
         if (var4 != null && var4.getName() != null && var4.getName().equalsIgnoreCase(var2)) {
            return var4;
         }
      }

      throw new DynamicCommandExceptionType(var0 -> Component.literal(String.format("Waypoint %s doesn't exists", var0))).create(var2);
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      ArrayList<NamedPosition> var3 = new ArrayList<>();

      for (NamedPosition var5 : (List<NamedPosition>)Hub.field_2604.getRegistry()) {
         if (var5 != null && (var5.method_106() == null || var5.method_600() == null || var5.getName() == null)) {
            var3.add(var5);
         }
      }

      for (NamedPosition var7 : var3) {
         Hub.field_2604.method_5(var7);
      }

      return SharedSuggestionProvider.suggest(((List<NamedPosition>)Hub.field_2604.getRegistry()).stream().map(NamedPosition::getName), var2);
   }
}
