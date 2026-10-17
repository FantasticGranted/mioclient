package me.mioclient.internal;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Type;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import me.mioclient.enum_.BindType;
import me.mioclient.mixin.ducks.DuckInputUtilType;
import me.mioclient.record.Keybind;
import net.minecraft.commands.SharedSuggestionProvider;

/** Brigadier ArgumentType parsing a key/mouse bind. */
public class KeybindArgumentType implements ArgumentType<Keybind> {
   public KeybindArgumentType() {
      super();
   }

   public Keybind parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var2 = var1.readString().toLowerCase(Locale.ROOT).trim();
      return var2.startsWith("mouse")
         ? new Keybind(Integer.parseInt(var2.toLowerCase().replace("mouse", "")), BindType.TOGGLE, true)
         : new Keybind(
            InputConstants.getKey(new PlaceholderText().method_2(var2.replace("_", ".")).method_9("key.keyboard.\u0001")).getValue(), BindType.TOGGLE, false
         );
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      ((DuckInputUtilType)(Object)Type.KEYSYM).getKeyMap().forEach((var1x, var2x) -> {
         String var3x = var2x.getName().replaceAll("key.keyboard.", "").replace(".", "_").toUpperCase();
         if (SharedSuggestionProvider.matchesSubStr(var2.getRemaining().toUpperCase(), var3x)) {
            var2.suggest(var3x);
         }
      });

      for (int var3 = 0; var3 < 5; var3++) {
         String var4 = new PlaceholderText().method_2(var3).method_9("MOUSE\u0001");
         if (SharedSuggestionProvider.matchesSubStr(var2.getRemaining(), var4)) {
            var2.suggest(var4);
         }
      }

      return var2.buildFuture();
   }
}
