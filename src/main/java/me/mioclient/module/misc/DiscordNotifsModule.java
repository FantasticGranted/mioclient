package me.mioclient.module.misc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.Priority;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_21;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.CommandManager;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import me.mioclient.nick.Settings;

public class DiscordNotifsModule extends Module {
   public Setting<Float> timeout;
   public Setting<Boolean> timestamp;
   public Setting<Boolean> queue;
   public Setting<Boolean> disconnect;
   public Setting<Boolean> dM;
   public Setting<Boolean> chatMessages;
   public Setting<Boolean> ignoreMio;
   public final DateTimeFormatter field_2546;
   public final List<String> field_2547;
   public final List<String> field_2548;
   public final Timer field_2549;
   public String field_2550;
   public ServerData field_2551;

   public DiscordNotifsModule() {
      super("DiscordNotifs", "Sends chat messages to your Discord webhook.", Category.MISC);
      Settings.initialize(this);
      this.field_2546 = DateTimeFormatter.ofPattern("HH:mm");
      this.field_2547 = Collections.synchronizedList(new ArrayList<>());
      this.field_2548 = new ArrayList<>();
      this.field_2549 = new Timer();
   }

   @Override
   public void onEnable() {
      this.field_2549.reset();
      if (Hub.field_2621.method_695() == null || Hub.field_2621.method_695().isEmpty()) {
         String var1 = CommandManager.method_927();
         String var2 = new PlaceholderText().method_2((Object)var1).method_9("\u0001webhook set <url>");
         ChatUtil.method_2(
            Component.literal("You don't have a webhook URL set. Set one by typing \"").append(var2).append("\"."), ChatUtil.method_38(-10395), Priority.MID
         );
      }
   }

   @Subscribe
   public void method_5(Event_21 var1) {
      if (mc.level != null) {
         this.field_2551 = Hub.field_2602.method_991();
      }

      if (Hub.field_2621.method_695() != null && !Hub.field_2621.method_695().isEmpty()) {
         if (this.field_2549.method_2((double)this.timeout.getValue().floatValue(), TimeUnit.SECONDS) && !this.field_2547.isEmpty()) {
            this.field_2549.reset();
            synchronized (this.field_2547) {
               StringBuilder var3 = new StringBuilder();
               LocalDateTime var4 = LocalDateTime.now();
               String var5 = "[%s] ".formatted(this.field_2546.format(var4));

               for (String var7 : this.field_2547) {
                  if (var3.length() > 1900) {
                     break;
                  }

                  if (this.timestamp.getValue()) {
                     var3.append(var5);
                  }

                  var3.append(var7);
                  var3.append("\n");
                  this.field_2548.add(var7);
               }

               String var11 = var3.toString();
               var11 = var11.substring(0, var11.length() - 1);
               Hub.field_2621.method_214("```%s```".formatted(var11));

               for (String var8 : this.field_2548) {
                  this.field_2547.remove(var8);
               }

               this.field_2548.clear();
            }
         }
      }
   }

   @Subscribe
   public void method_9(ChatMessageEvent var1) {
      if (!var1.method_464()) {
         if (Hub.field_2621.method_695() != null && !Hub.field_2621.method_695().isEmpty()) {
            if (var1.method_213() == PreType.PRE && var1.method_1035() != null) {
               if (this.ignoreMio.getValue() && var1.method_1036() == ChatUtil.field_3910) {
                  return;
               }

               String var2 = var1.method_1035();
               String[] var3 = var2.split("\n");
               boolean var4 = ChatUtil.method_107(var2);
               boolean var5 = ChatUtil.method_374(var2);
               if (!var4 && !var5) {
                  if (var2.contains("Position in queue:") && var3.length > 15) {
                     if (this.field_2550 == null) {
                        this.field_2550 = var2;
                     } else {
                        if (this.field_2550.equals(var2)) {
                           return;
                        }

                        if (this.queue.getValue()) {
                           this.method_7(var3);
                           this.field_2550 = var2;
                        }
                     }
                  } else if (this.chatMessages.getValue()) {
                     this.method_7(var3);
                  }
               } else if (this.dM.getValue()) {
                  this.method_7(var3);
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(DisconnectEvent var1) {
      if (this.disconnect.getValue() && this.field_2551 != null) {
         String var2 = "You have been disconnected from %s!".formatted(this.field_2551.ip);
         String[] var3 = var2.split("\n");
         this.method_7(var3);
         this.field_2551 = null;
      }
   }

   public void method_7(String[] var1) {
      StringBuilder var2 = new StringBuilder();

      for (String var6 : var1) {
         if (!var6.isEmpty()) {
            var2.append(ChatFormatting.stripFormatting(var6));
            var2.append("\n");
         }
      }

      String var7 = var2.toString();
      if (var7.length() >= 2) {
         var7 = var7.substring(0, var7.length() - 1);
         this.field_2547.add(var7);
      }
   }
}
