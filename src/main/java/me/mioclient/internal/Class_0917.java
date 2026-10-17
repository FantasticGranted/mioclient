package me.mioclient.internal;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0917 extends ChatCommand {
   public Class_0917() {
      super("yaw");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, Float>argument("yaw", FloatArgumentType.floatArg(-1.8e+02F, 1.8e+02F))
            .executes(
               var0 -> {
                  mc.player.setYRot((Float)var0.getArgument("yaw", Float.class));
                  ChatUtil.method_2(
                     Component.literal(
                        new PlaceholderText().method_2(String.valueOf(var0.getArgument("yaw", Float.class))).method_9("Player's yaw has been set to \u0001.")
                     ),
                     ChatUtil.method_38(-1)
                  );
                  return 1;
               }
            )
      );
   }
}
