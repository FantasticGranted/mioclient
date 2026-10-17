package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.mioclient.enum_.Priority;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_0820 extends Class_1344 {
   public Class_0820() {
      super("queue");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.executes(
         var1x -> {
            ChatUtil.method_2(Component.nullToEmpty("Fetching data..."), ChatUtil.method_22(this));
            this.method_29("queue", null)
               .whenComplete(
                  (var1xx, var2) -> {
                     Object var3 = null;
                     if (var2 != null) {
                        var3 = var2.getMessage();
                     } else {
                        try {
                           int var4 = -1337;
                           int var5 = -1337;
                           if (var1xx.has("prio")) {
                              var4 = var1xx.get("prio").getAsInt();
                           }

                           if (var1xx.has("regular")) {
                              var5 = var1xx.get("regular").getAsInt();
                           }

                           StringBuilder var6 = new StringBuilder();
                           if (var5 != -1337) {
                              var6.append("%d in normal queue".formatted(var5));
                           }

                           if (var5 != -1337 && var4 != -1337) {
                              var6.append(", ");
                           }

                           if (var4 != -1337) {
                              var6.append("%d in priority queue".formatted(var4));
                           }

                           ChatUtil.method_2(Component.nullToEmpty(var6.toString()), ChatUtil.method_22(this));
                           return;
                        } catch (Exception var7) {
                           var3 = var7.getMessage();
                        }
                     }

                     if (var3 != null) {
                        ChatUtil.method_2(
                           Component.nullToEmpty(new PlaceholderText().method_2(var3).method_2(String.valueOf(ChatFormatting.RED)).method_9("\u0001\u0001")),
                           ChatUtil.method_22(this),
                           Priority.MID
                        );
                     }
                  }
               );
            return 1;
         }
      );
   }
}
