package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import net.minecraft.commands.SharedSuggestionProvider;

/**
 * Brigadier argument type accepting only one of a fixed set of literal option strings,
 * suggesting the first option as a completion.
 */
public class ChoiceArgumentType implements ArgumentType<String> {
   public final String[] options;

   public ChoiceArgumentType(String... var1) {
      super();
      this.options = var1;
   }

   public String parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString();

      for (String var6 : this.options) {
         if (var6.equals(var2)) {
            return var6;
         }
      }

      throw CommandSyntaxException.BUILT_IN_EXCEPTIONS.literalIncorrect().createWithContext(var1, Arrays.toString((Object[])this.options));
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      String var3 = var2.getRemaining();

      for (String var7 : this.options) {
         if (var7.equals(var3)) {
            var2.suggest(var7);
            return var2.buildFuture();
         }
      }

      if (SharedSuggestionProvider.matchesSubStr(var2.getRemaining(), this.options[0])) {
         var2.suggest(this.options[0]);
      }

      return var2.buildFuture();
   }
}
