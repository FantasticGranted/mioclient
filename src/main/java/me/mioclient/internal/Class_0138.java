package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.mioclient.module.exploit.IllegalDisconnectModule;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0138 extends ChatCommand {
   public Class_0138() {
      super("disconnect");
      this.method_9("dc");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("illegal").executes(var1x -> {
         if (!this.method_535()) {
            IllegalDisconnectModule.method_203();
         }

         return 1;
      }))).executes(var1x -> {
         if (!this.method_535()) {
            mc.player.connection.getConnection().disconnect(Component.nullToEmpty("Disconnected"));
         }

         return 1;
      });
   }
}
