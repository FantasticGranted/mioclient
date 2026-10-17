package me.mioclient.module.client;

import java.awt.Color;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_1176;
import me.mioclient.enum_.Priority;
import me.mioclient.event.Event_15;
import me.mioclient.event.ModuleEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.FileUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.internal.FileManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.ChatVisiblity;
import me.mioclient.nick.Settings;
import org.apache.commons.io.FileUtils;

public class NotificationsModule extends Module {
   public static IRCModule field_906 = Hub.field_2595.method_78(IRCModule.class);
   public Setting<Color> watermark;
   public Setting<Color> brackets;
   public Setting<Boolean> modules;
   public Setting<Boolean> totemPops;
   public Setting<Boolean> self;
   public Setting<Color> primary;
   public Setting<Color> secondary;
   public Setting<Boolean> sayChat;
   public Setting<String> path;
   public Setting<Float> delay;
   public Setting<Boolean> random;
   public Setting<Boolean> refresh;
   public int field_2171;
   public final List<String> field_2172;
   public final Random field_2173;

   public NotificationsModule() {
      super("Notifications", "Lets you know about what's going on in chat.", Category.CLIENT);
      Settings.initialize(this);
      this.field_2171 = 0;
      this.field_2172 = new ArrayList<>();
      this.field_2173 = new Random();
      this.setDrawn(false);
      this.sayChat.method_9(() -> {
         if (this.sayChat.getValue()) {
            this.method_674();
         }
      });
      this.refresh.method_9(() -> {
         if (this.refresh.getValue()) {
            this.refresh.method_78(false);
            this.method_674();
         }
      });
      this.random.method_9(() -> this.field_2171 = 0);
      this.path.method_9(this::method_674);
   }

   @Override
   public void onEnable() {
      this.method_674();
   }

   @Subscribe
   public void method_2(Event_15 var1) {
      boolean var2 = mc.player == var1.method_1048();
      if (this.totemPops.getValue() && (!var2 || this.self.getValue())) {
         String var3 = var2 ? "You" : var1.getName();
         int var4 = var1.method_1049();
         if (var1.method_1050() == Class_1176.TOTEM_POP) {
            String var5 = var2 ? " have" : " has";
            this.method_2(var3, new PlaceholderText().method_2((Object)var5).method_9("\u0001 popped "), var4);
            if (!Hub.field_2603.method_1009(var3) && !var2 && this.sayChat.getValue() && !this.field_2172.isEmpty()) {
               THREAD_POOL.submit(() -> {
                  try {
                     if (this.delay.getValue() > 0.0F) {
                        Thread.sleep((long)(this.delay.getValue() * 1e+03F) + this.field_2173.nextLong(500L));
                     }

                     String var3x = null;
                     if (this.random.getValue()) {
                        if (this.field_2172.size() == 1) {
                           var3x = this.field_2172.get(0);
                        } else {
                           while (var3x == null) {
                              int var4x = this.field_2173.nextInt(this.field_2172.size());
                              if (this.field_2171 != var4x) {
                                 this.field_2171 = var4x;
                                 var3x = this.field_2172.get(var4x);
                              }
                           }
                        }
                     } else {
                        var3x = this.field_2172.get(this.field_2171);
                        this.field_2171 = (this.field_2171 + 1) % this.field_2172.size();
                     }

                     var3x = var3x.replace("{name}", var3);
                     var3x = var3x.replace("{totems}", Integer.toString(var4));
                     if (!field_906.isToggled() || !var3x.startsWith(field_906.prefix.getValue())) {
                        ChatUtil.method_425(var3x);
                     }
                  } catch (Exception var5x) {
                  }
               });
            }
         } else {
            this.method_2(var3, " has died after popping ", var4);
         }
      }
   }

   @Subscribe
   public void method_2(ModuleEvent var1) {
      if (!this.method_535() && this.modules.getValue() && !(var1.method_65() instanceof HUDModule)) {
         MutableComponent var2 = Component.empty();
         if (var1.method_65().isToggled()) {
            var2.append(Component.literal("[").withStyle(var1x -> var1x.withColor(this.method_263(true).darker().hashCode())));
            var2.append(Component.literal("+").withStyle(var1x -> var1x.withColor(this.method_263(true).hashCode())));
            var2.append(Component.literal("] ").withStyle(var1x -> var1x.withColor(this.method_263(true).darker().hashCode())));
         } else {
            var2.append(Component.literal("[").withStyle(var1x -> var1x.withColor(this.method_263(false).darker().hashCode())));
            var2.append(Component.literal("-").withStyle(var1x -> var1x.withColor(this.method_263(false).hashCode())));
            var2.append(Component.literal("] ").withStyle(var1x -> var1x.withColor(this.method_263(false).darker().hashCode())));
         }

         var2.append(Component.literal(Hub.field_2626.method_7(var1.method_65())));
         Hub.field_2619.method_2(() -> ChatUtil.method_2(var2, ChatUtil.method_2(var1.method_65())), 0);
      }
   }

   public String method_8(int var1) {
      return var1 == 1 ? " totem." : " totems.";
   }

   public void method_2(String var1, String var2, int var3) {
      if (mc.options.chatVisibility().get() != ChatVisiblity.HIDDEN) {
         MutableComponent var4 = Component.empty()
            .append(Component.literal(var1).withStyle(var1x -> var1x.withColor(this.primary.getValue().hashCode())))
            .append(Component.literal(var2).withStyle(var1x -> var1x.withColor(this.secondary.getValue().hashCode())))
            .append(Component.literal(String.valueOf(var3)).withStyle(var1x -> var1x.withColor(this.primary.getValue().hashCode())))
            .append(Component.literal(this.method_8(var3)).withStyle(var1x -> var1x.withColor(this.secondary.getValue().hashCode())));
         ChatUtil.method_2(var4, ChatUtil.method_38(Math.abs(var1.hashCode()) * -1), Priority.MID);
      }
   }

   public Color method_263(boolean var1) {
      return var1 ? new Color(0, 190, 50) : new Color(200, 0, 0);
   }

   public Color method_672() {
      return this.watermark.getValue();
   }

   public Color method_673() {
      return this.brackets.getValue();
   }

   public void method_674() {
      try {
         Path var1 = FileUtil.method_2(FileManager.field_4281.resolve(this.path.getValue()), new String[]{".txt"});
         if (!var1.toFile().exists()) {
            throw new RuntimeException("Invalid totem pop file path");
         }

         this.field_2171 = 0;
         this.field_2172.clear();
         this.field_2172.addAll(FileUtils.readLines(var1.toFile(), StandardCharsets.UTF_8));
      } catch (Exception var2) {
         if (this.sayChat.getValue()) {
            ChatUtil.method_2(
               Component.literal("Failed to open totem pop file").withStyle(var0 -> var0.withColor(ChatFormatting.RED)),
               ChatUtil.method_38(var2.toString().hashCode()),
               Priority.LOW
            );
            var2.printStackTrace();
         }
      }
   }
}
