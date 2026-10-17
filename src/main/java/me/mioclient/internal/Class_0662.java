package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.mioclient.enum_.Priority;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0662 extends ChatCommand {
   public Class_0662() {
      super("print");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
            RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("message", StringArgumentType.greedyString())
               .executes(
                  var0 -> {
                     mc.gui
                        .getChat()
                        .addPlayerMessage(
                           Component.nullToEmpty((String)var0.getArgument("message", String.class)),
                           ChatUtil.method_38((int)(Math.random() * 1e+04)),
                           ChatUtil.field_3910
                        );
                     return 1;
                  }
               )
         ))
         .executes(var0 -> {
            ChatUtil.method_2(Component.literal("Please enter a message to print."), ChatUtil.method_38(-1), Priority.LOW);
            return 1;
         });
   }
}
