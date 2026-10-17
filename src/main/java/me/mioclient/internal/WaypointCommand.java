package me.mioclient.internal;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Stream;
import me.mioclient.Hub;
import me.mioclient.enum_.Dimension;
import me.mioclient.module.render.WaypointsModule;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.Vec3;

/** Chat command to add/remove/list waypoints. */
public final class WaypointCommand extends ChatCommand {
   public WaypointCommand() {
      super("waypoints");
      this.method_9("wp");
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      ((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)((LiteralArgumentBuilder<SharedSuggestionProvider>)var1.then(
                  LiteralArgumentBuilder.<SharedSuggestionProvider>literal("add")
                     .then(
                        ((RequiredArgumentBuilder<SharedSuggestionProvider, ?>)RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string())
                              .then(((RequiredArgumentBuilder<SharedSuggestionProvider, ?>)RequiredArgumentBuilder.<SharedSuggestionProvider, Vec3>argument("pos", new PositionArgumentType()).executes(var0 -> {
                                 String var1x = (String)var0.getArgument("name", String.class);
                                 Vec3 var2 = (Vec3)var0.getArgument("pos", Vec3.class);
                                 String var3 = BlockUtil.method_1071().getDisplayName().toLowerCase();
                                 NamedPosition var4 = new NamedPosition(var1x, var2, var3, method_1054());
                                 Hub.field_2604.method_9(var4);
                                 ChatUtil.method_2(Component.literal("Waypoint ").append(var4.method_15()).append(" has been created"), -1);
                                 return 1;
                              })).then(RequiredArgumentBuilder.<SharedSuggestionProvider, Dimension>argument("dimension", new EnumArgumentType<>(Dimension.class, "dimension")).executes(var0 -> {
                                 String var1x = (String)var0.getArgument("name", String.class);
                                 Vec3 var2 = (Vec3)var0.getArgument("pos", Vec3.class);
                                 Dimension var3 = (Dimension)var0.getArgument("dimension", Dimension.class);
                                 NamedPosition var4 = new NamedPosition(var1x, var2, var3.getDisplayName().toLowerCase(), method_1054());
                                 Hub.field_2604.method_9(var4);
                                 ChatUtil.method_2(Component.literal("Waypoint ").append(var4.method_15()).append(" has been created"), -1);
                                 return 1;
                              }))))
                           .executes(var0 -> {
                              String var1x = (String)var0.getArgument("name", String.class);
                              String var2 = BlockUtil.method_1071().getDisplayName().toLowerCase();
                              Vec3 var3 = mc.player.position();
                              NamedPosition var4 = new NamedPosition(var1x, var3, var2, method_1054());
                              Hub.field_2604.method_9(var4);
                              ChatUtil.method_2(Component.literal("Waypoint ").append(var4.method_15()).append(" has been created"), -1);
                              return 1;
                           })
                     )
               ))
               .then(
                  ChatCommand.method_2("remove", "delete", "del")
                     .then(
                        RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("server", StringArgumentType.string())
                           .suggests(WaypointCommand::method_38)
                           .then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string()).suggests(WaypointCommand::method_78).executes(var0 -> {
                              String var1x = (String)var0.getArgument("name", String.class);
                              String var2 = (String)var0.getArgument("server", String.class);
                              ((List<NamedPosition>)Hub.field_2604.getRegistry())
                                 .removeIf(var2x -> var2.equalsIgnoreCase(var2x.method_106()) && var1x.equalsIgnoreCase(var2x.getName()));
                              ChatUtil.method_2(Component.literal("Waypoint ").append(var1x).append(" has been removed"), -1);
                              return 1;
                           }))
                     )
               ))
            .then(
               LiteralArgumentBuilder.<SharedSuggestionProvider>literal("rename")
                  .then(
                     RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("server", StringArgumentType.string())
                        .suggests(WaypointCommand::method_38)
                        .then(
                           RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string())
                              .suggests(WaypointCommand::method_78)
                              .then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("target", StringArgumentType.string()).executes(var0 -> {
                                 String var1x = (String)var0.getArgument("name", String.class);
                                 String var2 = (String)var0.getArgument("server", String.class);
                                 String var3 = (String)var0.getArgument("target", String.class);
                                 Predicate<NamedPosition> var4 = var2x -> var2.equalsIgnoreCase(var2x.method_106()) && var1x.equalsIgnoreCase(var2x.getName());
                                 NamedPosition var5 = (NamedPosition)Hub.field_2604.method_2(var4).orElse(null);
                                 if (var5 == null) {
                                    ChatUtil.method_2(Component.nullToEmpty("Waypoint not found"), -1);
                                    return 1;
                                 } else {
                                    ((List<NamedPosition>)Hub.field_2604.getRegistry()).removeIf(var4);
                                    ((List<NamedPosition>)Hub.field_2604.getRegistry()).add(new NamedPosition(var3, var5.method_733(), var5.method_600(), var5.method_106()));
                                    MutableComponent var6 = Component.literal("Waypoint ");
                                    var6 = var6.append(var1x);
                                    var6 = var6.append(" has been renamed to");
                                    var6 = var6.append(var3);
                                    ChatUtil.method_2(var6, -1);
                                    return 1;
                                 }
                              }))
                        )
                  )
            ))
         .then(LiteralArgumentBuilder.<SharedSuggestionProvider>literal("list").executes(var0 -> {
            MutableComponent var1x = Component.empty();
            var1x.append("Waypoints list: ");
            var1x.append(ComponentUtils.formatList(((List<NamedPosition>)Hub.field_2604.getRegistry()).stream().map(NamedPosition::method_15).toList(), Component.literal(", ")));
            ChatUtil.method_2(var1x, -1);
            return 1;
         }));
      var1.then(
         LiteralArgumentBuilder.<SharedSuggestionProvider>literal("toggle")
            .then(
               RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("server", StringArgumentType.string())
                  .suggests(WaypointCommand::method_38)
                  .then(RequiredArgumentBuilder.<SharedSuggestionProvider, String>argument("name", StringArgumentType.string()).suggests(WaypointCommand::method_78).executes(var0 -> {
                     String var1x = (String)var0.getArgument("name", String.class);
                     String var2 = (String)var0.getArgument("server", String.class);
                     Predicate<NamedPosition> var3 = var2x -> var2.equalsIgnoreCase(var2x.method_106()) && var1x.equalsIgnoreCase(var2x.getName());
                     NamedPosition var4 = (NamedPosition)Hub.field_2604.method_2(var3).orElse(null);
                     if (var4 == null) {
                        ChatUtil.method_2(Component.nullToEmpty("Waypoint not found"), -1);
                        return 1;
                     } else {
                        var4.method_38(!var4.isToggled());
                        String var5 = "Made the waypoint %s %s.".formatted(var1x, var4.isToggled() ? "visible" : "invisible");
                        ChatUtil.method_2(Component.literal(var5), -1);
                        return 1;
                     }
                  }))
            )
      );
      if (Hub.field_2630.method_264()) {
         this.method_2(new Class_0393(), var1);
         this.method_2(new Class_0412(), var1);
      }
   }

   public static CompletableFuture<Suggestions> method_38(CommandContext<SharedSuggestionProvider> var0, SuggestionsBuilder var1) {
      return SharedSuggestionProvider.suggest(((List<NamedPosition>)Hub.field_2604.getRegistry()).stream().map(WaypointCommand::method_2).distinct(), var1);
   }

   public static CompletableFuture<Suggestions> method_2(CommandContext<SharedSuggestionProvider> var0, SuggestionsBuilder var1, String var2) {
      Stream var3 = ((List<NamedPosition>)Hub.field_2604.getRegistry()).stream().filter(var1x -> var1x.method_106().equalsIgnoreCase(var2)).map(NamedPosition::getName);
      return SharedSuggestionProvider.suggest(var3, var1);
   }

   public static CompletableFuture<Suggestions> method_78(CommandContext<SharedSuggestionProvider> var0, SuggestionsBuilder var1) {
      String var2 = (String)var0.getArgument("server", String.class);
      return method_2(var0, var1, var2);
   }

   public static String method_2(NamedPosition var0) {
      String var1 = var0.method_106().toLowerCase();
      return var1.contains(":") ? new PlaceholderText().method_2((Object)var1).method_9("\"\u0001\"") : var1;
   }

   public static String method_1054() {
      ServerData var0 = mc.player.connection.getServerData();
      if (var0 == null) {
         return "singleplayer";
      } else {
         String[] var1 = var0.ip.split(":");
         return var1.length == 2 && var1[1].equalsIgnoreCase("25565") ? var1[0] : var0.ip;
      }
   }

   public static Vec3 method_107(CommandContext<SharedSuggestionProvider> var0) {
      String var1 = (String)var0.getArgument("name", String.class);
      NamedPosition var2 = Hub.field_2604.method_2(var1x -> var1x.method_16(var1, method_1054())).orElse(null);
      return var2 == null ? null : WaypointsModule.method_4(var2);
   }
}
