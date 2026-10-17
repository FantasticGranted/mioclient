package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import me.mioclient.api.MinecraftAccess;
import net.minecraft.commands.SharedSuggestionProvider;

/** Brigadier String argument that suggests a preset list of names. */
public class NameArgumentType implements ArgumentType<String>, MinecraftAccess {
   public static final List<String> field_692 = List.of("cat", "fit", "asphyxia1337");

   public NameArgumentType() {
      super();
   }

   public String parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      return var1.readString();
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      return SharedSuggestionProvider.suggest(mc.player.connection.getOnlinePlayers().stream().map(var0 -> var0.getProfile().name()), var2);
   }

   public Collection<String> getExamples() {
      return field_692;
   }
}
