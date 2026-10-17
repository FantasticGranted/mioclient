package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.concurrent.CompletableFuture;
import me.mioclient.Hub;
import me.mioclient.deobf.Named;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

/**
 * Abstract base for chat commands: holds aliases and a brigadier exec() builder,
 * registering each subclass as a runnable client-side command.
 */
public abstract class ChatCommand extends Named {
   public String[] aliases = new String[0];

   public ChatCommand(String var1) {
      super(var1);
   }

   public static RequiredArgumentBuilder<SharedSuggestionProvider, String> method_2(String... var0) {
      return RequiredArgumentBuilder.argument(var0[0], new ChoiceArgumentType(var0));
   }

   public static CompletableFuture<Suggestions> method_2(SuggestionsBuilder var0, String... var1) {
      for (String var5 : var1) {
         var0.suggest(var5);
      }

      return var0.buildFuture();
   }

   public void method_2(Runnable var1) {
      Hub.field_2619.method_2(var1, 0);
   }

   public MutableComponent method_5(String var1) {
      return Component.empty().append(ChatUtil.method_5(var1));
   }

   public void method_9(String... var1) {
      this.aliases = var1;
   }

   public String[] getAliases() {
      return this.aliases;
   }

   public abstract void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1);

   public void method_2(ChatCommand var1, LiteralArgumentBuilder<SharedSuggestionProvider> var2) {
      LiteralArgumentBuilder var3 = LiteralArgumentBuilder.<SharedSuggestionProvider>literal(var1.getName());
      var1.exec(var3);
      var2.then(var3);
   }
}
