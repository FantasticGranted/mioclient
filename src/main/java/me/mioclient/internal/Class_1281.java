package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.mioclient.Hub;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_1281 extends ChatCommand {
   public Class_1281() {
      super("ignore");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
            LiteralArgumentBuilder.<SharedSuggestionProvider>literal("add").then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.word()).executes(var0 -> {
               String var1x = ((String)var0.getArgument("name", String.class)).toLowerCase();
               if (Hub.field_2610.method_331().contains(var1x)) {
                  ChatUtil.method_2(
                     Component.literal(new PlaceholderText().method_2((Object)var1x).method_9("You are already ignoring \u0001")), ChatUtil.method_38(-1)
                  );
               } else {
                  Hub.field_2610.method_331().add(var1x);
                  ChatUtil.method_2(
                     Component.literal(new PlaceholderText().method_2((Object)var1x).method_9("Player \u0001 has been ignored")), ChatUtil.method_38(-1)
                  );
               }

               return 1;
            }))
         ))
         .then(
            LiteralArgumentBuilder.<SharedSuggestionProvider>literal("remove")
               .then(
                  RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.word())
                     .suggests((var0, var1x) -> SharedSuggestionProvider.suggest(Hub.field_2610.method_331(), var1x))
                     .executes(var0 -> {
                        String var1x = ((String)var0.getArgument("name", String.class)).toLowerCase();
                        if (Hub.field_2610.method_331().contains(var1x)) {
                           ChatUtil.method_2(
                              Component.literal(new PlaceholderText().method_2((Object)var1x).method_9("\u0001 is no longer ignored")), ChatUtil.method_38(-1)
                           );
                           Hub.field_2610.method_331().remove(var1x);
                        } else {
                           ChatUtil.method_2(
                              Component.literal(new PlaceholderText().method_2((Object)var1x).method_9("You are not ignoring \u0001")), ChatUtil.method_38(-1)
                           );
                        }

                        return 1;
                     })
               )
         );
   }
}
