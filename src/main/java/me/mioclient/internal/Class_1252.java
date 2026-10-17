package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import me.mioclient.enum_.Priority;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;

public class Class_1252 extends Class_1344 {
   public final DateTimeFormatter field_3918 = DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm:ss", Locale.ENGLISH);

   public Class_1252() {
      super("seen");
      this.method_9("lastseen");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.then(
         RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.greedyString())
            .executes(
               var1x -> {
                  String var2 = (String)var1x.getArgument("name", String.class);
                  ChatUtil.method_2(Component.nullToEmpty("Fetching data..."), ChatUtil.method_22(this));
                  this.method_29("seen", var2)
                     .whenComplete(
                        (var2x, var3) -> {
                           Object var4 = null;
                           if (var3 != null) {
                              var4 = var3.getMessage();
                           } else {
                              try {
                                 if (var2x.has("lastSeen")) {
                                    if (var2x.get("lastSeen").isJsonNull()) {
                                       throw new RuntimeException("No data for player.");
                                    }

                                    String var5 = var2x.get("lastSeen").getAsString();
                                    OffsetDateTime var6 = OffsetDateTime.parse(var5);
                                    OffsetDateTime var7 = OffsetDateTime.now(ZoneOffset.UTC);
                                    Duration var8 = Duration.between(var6, var7);
                                    StringBuilder var9 = new StringBuilder(var2);
                                    int var10 = (int)var8.toSeconds();
                                    if (var10 == 0) {
                                       var9.append(" is online right now.");
                                    } else {
                                       var9.append(" was seen ");
                                       var9.append(Class_1344.method_105((int)var8.toSeconds()));
                                       var9.append(" ago (");
                                       var9.append(this.field_3918.format(var6));
                                       var9.append(").");
                                    }

                                    ChatUtil.method_2(Component.nullToEmpty(var9.toString()), ChatUtil.method_22(this));
                                    return;
                                 }

                                 var4 = "Response data doesn't have a required field: lastSeen.";
                              } catch (Exception var11) {
                                 var4 = var11.getMessage();
                              }
                           }

                           if (var4 != null) {
                              ChatUtil.method_2(
                                 Component.nullToEmpty(new PlaceholderText().method_2(var4).method_2(String.valueOf(ChatFormatting.RED)).method_9("\u0001\u0001")),
                                 ChatUtil.method_22(this),
                                 Priority.MID
                              );
                           }
                        }
                     );
                  return 1;
               }
            )
      );
   }
}
