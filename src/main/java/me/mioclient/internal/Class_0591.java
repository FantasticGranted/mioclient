package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import me.mioclient.mixin.ducks.DuckSession;
import net.minecraft.client.gui.screens.ConnectScreen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.multiplayer.resolver.ServerAddress;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0591 extends ChatCommand {
   public Class_0591() {
      super("session");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string())
            .executes(
               var0 -> {
                  String var1x = (String)var0.getArgument("name", String.class);
                  if (var1x.length() > 16) {
                     return 0;
                  } else {
                     if (mc.getConnection().getServerData() != null) {
                        mc.getConnection().getConnection().disconnect(Component.nullToEmpty(""));
                        ServerData var2 = mc.getConnection().getServerData();
                        ConnectScreen.startConnecting(new JoinMultiplayerScreen(new TitleScreen()), mc, ServerAddress.parseString(var2.ip), var2, true, null);
                     }

                     ((DuckSession)(Object)mc.getUser()).setUsername(var1x);
                     ChatUtil.method_2(
                        Component.literal(new PlaceholderText().method_2((Object)var1x).method_9("Session's name has been set to \u0001.")), ChatUtil.method_38(-1)
                     );
                     return 1;
                  }
               }
            )
      );
   }
}
