package me.mioclient.internal;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0732 extends ChatCommand {
   public Class_0732() {
      super("pitch");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, Float>argument("pitch", FloatArgumentType.floatArg((float)(-Class_0245.field_685), (float)Class_0245.field_685))
            .executes(
               var0 -> {
                  mc.player.setXRot((Float)var0.getArgument("pitch", Float.class));
                  ChatUtil.method_2(
                     Component.literal(
                        new PlaceholderText().method_2(String.valueOf(var0.getArgument("pitch", Float.class))).method_9("Player's pitch has been set to \u0001.")
                     ),
                     ChatUtil.method_38(-1)
                  );
                  return 1;
               }
            )
      );
   }
}
