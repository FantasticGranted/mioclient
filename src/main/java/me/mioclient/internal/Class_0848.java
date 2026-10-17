package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public final class Class_0848 extends ChatCommand {
   public Class_0848() {
      super("coords");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", new NameArgumentType()).executes(var1x -> {
         this.method_9((String)var1x.getArgument("name", String.class), this.method_793());
         return 1;
      }))).executes(var1x -> {
         try {
            mc.keyboardHandler.setClipboard(this.method_793());
            ChatUtil.method_2(Component.literal("Your position has been copied to the clipboard."), ChatUtil.method_38(-1));
         } catch (Exception var3) {
         }

         return 1;
      });
   }

   public String method_793() {
      return mc.player == null
         ? ""
         : new PlaceholderText()
            .method_2((int)Math.floor(Math.random() * 1e+04))
            .method_2(BlockUtil.method_1071().getDisplayName())
            .method_2((int)mc.player.getZ())
            .method_2((int)mc.player.getY())
            .method_2((int)mc.player.getX())
            .method_9("X: \u0001, Y: \u0001, Z: \u0001 in The \u0001 [\u0001]");
   }

   public void method_9(String var1, String var2) {
      String var3 = new PlaceholderText().method_2((Object)var2).method_2((Object)var1).method_9("w \u0001 \u0001");
      mc.player.connection.sendCommand(var3);
   }
}
