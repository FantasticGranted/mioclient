package me.mioclient.internal;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;
import me.mioclient.api.JsonConfig;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

/**
 * Brigadier argument type for a module setting's name: suggests the module's setting names
 * and resolves an argument string back to the matching {@link Setting}.
 */
public class SettingNameArgument implements ArgumentType<String>, MinecraftAccess {
   public final Module module;

   public SettingNameArgument(Module var1) {
      super();
      this.module = var1;
   }

   public String parse(StringReader var1) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      return var1.readString();
   }

   public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> var1, SuggestionsBuilder var2) {
      Stream var3 = this.module.getRegistry().stream().filter(var0 -> !var0.method_158()).map(JsonConfig::getConfigName);
      return SharedSuggestionProvider.suggest(var3, var2);
   }

   public static Setting<?> getOption(CommandContext<?> var0, Module var1, String var2) throws com.mojang.brigadier.exceptions.CommandSyntaxException {
      String var3 = (String)var0.getArgument(var2, String.class);
      Optional var4 = var1.getRegistry().stream().filter(var1x -> var1x.getConfigName().equalsIgnoreCase(var3)).findFirst();
      if (var4.isEmpty()) {
         throw new DynamicCommandExceptionType(var0x -> Component.literal(new PlaceholderText().method_2(String.valueOf(var0x)).method_9("Setting not found \u0001")))
            .create(var3);
      } else {
         return (Setting<?>)var4.get();
      }
   }
}
