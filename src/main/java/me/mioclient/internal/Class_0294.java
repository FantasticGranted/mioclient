package me.mioclient.internal;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.tree.CommandNode;
import java.util.HashSet;
import java.util.List;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;
import net.minecraft.network.protocol.game.ClientboundCommandSuggestionsPacket;
import net.minecraft.network.protocol.game.ServerboundCommandSuggestionPacket;

public final class Class_0294 extends ChatCommand {
   public final Timer stopwatch = new Timer();
   public boolean field_947;

   public Class_0294() {
      super("plugins");
      lookups.method_14(this);
   }

   @Override
   public void exec(LiteralArgumentBuilder<SharedSuggestionProvider> var1) {
      var1.executes(var1x -> {
         mc.player.connection.send(new ServerboundCommandSuggestionPacket(0, "/"));
         ChatUtil.method_2(Component.literal("Fetching plugins..."), ChatUtil.method_38(-3));
         this.stopwatch.reset();
         this.field_947 = true;
         return 1;
      });
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (this.field_947) {
         if (this.stopwatch.method_9(3000L)) {
            this.field_947 = false;
            List var2 = mc.player.connection.getCommands().getRoot().getChildren().stream().map(CommandNode::getName).toList();
            if (var2.isEmpty()) {
               ChatUtil.method_2(Component.literal("Couldn't get plugins"), ChatUtil.method_38(-3));
            } else {
               ChatUtil.method_2(this.method_5(var2), ChatUtil.method_38(-3));
            }
         }

         if (var1.method_127() instanceof ClientboundCommandSuggestionsPacket var4) {
            this.field_947 = false;
            ChatUtil.method_2(this.method_5(var4.toSuggestions().getList().stream().<String>map(Suggestion::getText).toList()), ChatUtil.method_38(-3));
         }
      }
   }

   public Component method_5(Iterable<String> var1) {
      HashSet<String> var2 = new HashSet<>();

      for (String var4 : var1) {
         String[] var5 = var4.split(":");
         if (var5.length > 1 && !var5[0].isEmpty()) {
            var2.add(var5[0]);
         }
      }

      Component var6 = ComponentUtils.formatList(var2, var0 -> Component.literal(var0).withStyle(var0x -> ChatUtil.method_2(var0x, () -> Colors.method_959().hashCode())));
      return Component.empty().append("Found plugins [").append(Component.literal(String.valueOf(var2.size())).withStyle(ChatFormatting.GRAY)).append("]: ").append(var6);
   }
}
