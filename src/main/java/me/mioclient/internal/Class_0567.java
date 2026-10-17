package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.List;
import me.mioclient.Hub;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0567 extends ChatCommand {
   public Class_0567() {
      super("kit");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(ChatCommand.method_2("save", "add").then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string()).executes(var0 -> {
         String var1x = (String)var0.getArgument("name", String.class);
         Hub.field_2625.method_468(var1x);
         ChatUtil.method_2(Component.literal("Kit %s has been saved".formatted(var1x)), ChatUtil.method_38(-1));
         return 1;
      })));
      var1.then(ChatCommand.method_2("remove", "delete", "del").then(RequiredArgumentBuilder.<SharedSuggestionProvider, ItemMatcherProfile>argument("kit", new Class_1363()).executes(var0 -> {
         ItemMatcherProfile var1x = (ItemMatcherProfile)var0.getArgument("kit", ItemMatcherProfile.class);
         ((List<ItemMatcherProfile>)Hub.field_2625.getRegistry()).removeIf(var1xx -> var1x.getName().equalsIgnoreCase(var1xx.getName()));
         ChatUtil.method_2(Component.literal("Kit %s has been removed".formatted(var1x.getName())), ChatUtil.method_38(-1));
         return 1;
      })));
      var1.then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("load").then(RequiredArgumentBuilder.<SharedSuggestionProvider, ItemMatcherProfile>argument("kit", new Class_1363()).executes(var0 -> {
         ItemMatcherProfile var1x = (ItemMatcherProfile)var0.getArgument("kit", ItemMatcherProfile.class);
         Hub.field_2625.method_27(var1x.getName());
         ChatUtil.method_2(Component.literal("Kit %s has been loaded".formatted(var1x.getName())), ChatUtil.method_38(-1));
         return 1;
      })));
   }
}
