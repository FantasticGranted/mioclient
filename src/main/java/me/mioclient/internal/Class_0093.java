package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.module.misc.ChatFilterModule;
import me.mioclient.record.StringPair;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0093 extends ChatCommand {
   public static ChatFilterModule field_302 = Hub.field_2595.method_78(ChatFilterModule.class);

   public Class_0093() {
      super("chatfilter");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
               LiteralArgumentBuilder.<SharedSuggestionProvider>literal("add")
                  .then(
                     RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("id", StringArgumentType.string())
                        .then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("filter", StringArgumentType.string()).executes(var0 -> {
                           String var1x = (String)var0.getArgument("id", String.class);
                           String var2 = (String)var0.getArgument("filter", String.class);
                           if (Hub.field_2627.method_334(var1x)) {
                              ChatUtil.method_2(Component.nullToEmpty("Chat filter \"%s\" already exists.".formatted(var1x)), ChatUtil.method_38(-3469856), Priority.MID);
                           } else {
                              Hub.field_2627.method_38(var1x, var2);
                              ChatUtil.method_2(Component.nullToEmpty("Chat filter \"%s\" has been added.".formatted(var1x)), ChatUtil.method_38(-3469857));
                           }

                           return 1;
                        }))
                  )
            ))
            .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("remove").then(RequiredArgumentBuilder.<SharedSuggestionProvider, StringPair>argument("id", new Class_0882()).executes(var0 -> {
               StringPair var1x = (StringPair)var0.getArgument("id", StringPair.class);
               Hub.field_2627.method_2(var1x);
               ChatUtil.method_2(Component.nullToEmpty("Chat filter \"%s\" has been removed.".formatted(var1x.getFirst())), ChatUtil.method_38(-3469858));
               return 1;
            }))))
         .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("list").executes(var0 -> {
            ChatUtil.method_2(Component.nullToEmpty("%d chat filter(s)".formatted(Hub.field_2627.method_336())), ChatUtil.method_38(-3569855));
            int var1x = -3469852;

            for (String var4 : Hub.field_2627.method_337()) {
               ChatUtil.method_2(Component.nullToEmpty(var4), ChatUtil.method_38(++var1x));
            }

            return 1;
         }));
   }
}
