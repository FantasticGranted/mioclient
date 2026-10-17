package me.mioclient.internal;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Locale;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.mioclient.Hub;
import me.mioclient.api.StyleColorAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Priority;
import me.mioclient.module.Module;
import me.mioclient.module.client.NotificationsModule;
import net.minecraft.ChatFormatting;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.ClickEvent.Action;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;

/**
 * Chat helpers: detect incoming/outgoing whispers, build the colored "[Mio]" prefix,
 * and add client-only messages to the chat HUD with synthetic signatures.
 */
public class ChatUtil implements MinecraftAccess {
   public static final NotificationsModule notifications = Hub.field_2595.method_78(NotificationsModule.class);
   public static final GuiMessageTag field_3910 = new GuiMessageTag(-1, null, Component.empty(), "Mio");
   public static final Pattern dmPattern = Pattern.compile("^(\\w{3,16}) -> (\\w{3,16}): (.*)");
   public static final List<Pattern> incomingDmPatterns = List.of(
      Pattern.compile("^From (\\w{3,16}): (.*)"),
      Pattern.compile("^from (\\w{3,16}): (.*)"),
      Pattern.compile("^(\\w{3,16}) whispers: (.*)"),
      Pattern.compile("^(\\w{3,16}) -> me (.*)"),
      Pattern.compile("^(\\w{3,16}) whispers to you: (.*)"),
      Pattern.compile("^(\\w{3,16}) says: (.*)"),
      Pattern.compile("^(\\w{3,16}) пишет: (.*)"),
      dmPattern
   );
   public static final List<Pattern> outgoingDmPatterns = List.of(
      Pattern.compile("^To (\\w{3,16}): (.*)"),
      Pattern.compile("^to (\\w{3,16}): (.*)"),
      Pattern.compile("^\\[me -> (\\w{3,16})\\] (.*)"),
      Pattern.compile("^You whisper to (\\w{3,16}): (.*)"),
      Pattern.compile("^К (\\w{3,16}): (.*)"),
      dmPattern
   );

   public ChatUtil() {
      super();
   }

   public static boolean method_107(String var0) {
      if (var0.toLowerCase(Locale.ROOT).startsWith(mc.getUser().getName().toLowerCase(Locale.ROOT))) {
         return false;
      } else if (var0.contains(" шепчет: ")) {
         return true;
      } else if (var0.contains(" шепчет тебе: ")) {
         return true;
      } else {
         for (Pattern var2 : incomingDmPatterns) {
            Matcher var3 = var2.matcher(var0);
            if (var3.find()) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean method_374(String var0) {
      boolean var1 = var0.toLowerCase(Locale.ROOT).startsWith(mc.getUser().getName().toLowerCase(Locale.ROOT));

      for (Pattern var3 : outgoingDmPatterns) {
         if (var3 != dmPattern || var1) {
            Matcher var4 = var3.matcher(var0);
            if (var4.find()) {
               return true;
            }
         }
      }

      return false;
   }

   public static MessageSignature method_38(int var0) {
      return new MessageSignature(ByteBuffer.allocate(256).putInt(var0).array());
   }

   public static MessageSignature method_22(Object var0) {
      return method_38(Math.abs(var0.hashCode()) * -1);
   }

   public static MessageSignature method_2(Module var0) {
      return method_38(Math.abs(var0.getName().hashCode()) * -1);
   }

   public static Style method_2(Style var0, Supplier<Integer> var1) {
      return ((StyleColorAccess)(Object)var0.withColor((Integer)var1.get())).mio$withColor(var1);
   }

   public static MutableComponent method_5(String var0) {
      return var0 == null ? Component.empty() : Component.literal(var0).withStyle(var0x -> method_2(var0x, () -> notifications.watermark.getValue().hashCode()));
   }

   public static Component method_287() {
      MutableComponent var0 = Component.empty();
      var0.append(Component.literal("[").withStyle(var0x -> method_2(var0x, () -> notifications.method_673().hashCode())));
      var0.append(Component.literal("Mio").withStyle(var0x -> method_2(var0x, () -> notifications.method_672().hashCode())));
      var0.append(Component.literal("]").withStyle(var0x -> method_2(var0x, () -> notifications.method_673().hashCode())));
      return var0;
   }

   public static void method_2(Component var0, int var1) {
      method_2(var0, method_38(var1));
   }

   public static void method_2(Component var0, MessageSignature var1) {
      mc.execute(() -> {
         try {
            mc.gui.getChat().addPlayerMessage(Component.empty().append(method_287()).append(" ").append(var0), var1, field_3910);
         } catch (Throwable var3) {
         }
      });
   }

   public static void method_2(Component var0, MessageSignature var1, Priority var2) {
      MutableComponent var3 = Component.empty();
      var3.append(Component.literal("[").withStyle(var1x -> var1x.withColor(var2.method_152().darker().hashCode())));
      var3.append(Component.literal("!").withStyle(var1x -> var1x.withColor(var2.method_152().hashCode())));
      var3.append(Component.literal("] ").withStyle(var1x -> var1x.withColor(var2.method_152().darker().hashCode())));
      method_2(var3.append(var0), var1);
   }

   public static Component method_2(String var0, Supplier<Integer> var1, Color var2) {
      MutableComponent var3 = Component.empty();
      float[] var4 = Color.RGBtoHSB(var2.getRed(), var2.getGreen(), var2.getBlue(), null);

      for (int var5 = 0; var5 < var0.length(); var5++) {
         int var6 = var5;
         var3.append(
            Component.literal(String.valueOf(var0.charAt(var5)))
               .withStyle(var3x -> method_2(var3x, () -> Colors.method_2(var6 * (Integer)var1.get(), var4[1], var4[2], 255).hashCode()))
         );
      }

      return var3;
   }

   public static void method_425(String var0) {
      if (var0.startsWith("/")) {
         mc.player.connection.sendCommand(var0.substring(1));
      } else {
         mc.player.connection.sendChat(var0);
      }
   }

   public static ClickEvent method_52(String var0) {
      return new ClickEvent.SuggestCommand(new PlaceholderText().method_2((Object)var0).method_2(CommandManager.method_927()).method_9("\u0001\u0001"));
   }

   public static Component method_185(String var0) {
      return Component.empty().append(Component.literal("[").withStyle(ChatFormatting.GRAY)).append(var0).append(Component.literal("]").withStyle(ChatFormatting.GRAY));
   }
}
