package me.mioclient.module.misc;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.enum_.Priority;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.FileUtil;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.FileManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.client.IRCModule;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import me.mioclient.nick.Settings;
import org.apache.commons.io.FileUtils;

public class SpammerModule extends Module {
   public Setting<String> path;
   public Setting<Float> delay;
   public Setting<Boolean> refresh;
   public Setting<Boolean> smartDelay;
   public Setting<Boolean> random;
   public Setting<Boolean> autoOff;
   public static IRCModule field_906 = Hub.field_2595.method_78(IRCModule.class);
   public final Random field_1830;
   public final List<String> field_1831;
   public final Timer field_1832;
   public int current;

   public SpammerModule() {
      super("Spammer", "Spams messages from a selected text file.", Category.MISC);
      Settings.initialize(this);
      this.field_1830 = new Random();
      this.field_1831 = new ArrayList<>();
      this.field_1832 = new Timer();
      this.current = 0;
      this.path.method_9(this::method_142);
      this.refresh.method_9(() -> {
         if (this.refresh.getValue()) {
            this.refresh.method_78(false);
            this.method_142();
         }
      });
   }

   @Override
   public void onEnable() {
      this.method_142();
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundChatPacket && this.smartDelay.getValue()) {
         this.field_1832.reset();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.field_1832.method_2((double)this.delay.getValue().floatValue(), TimeUnit.SECONDS) && !this.field_1831.isEmpty()) {
         String var2 = this.field_1831
            .get(this.random.getValue() && this.field_1831.size() > 1 ? this.field_1830.nextInt(this.field_1831.size() - 1) : this.current);
         if (!field_906.isToggled() || !var2.startsWith(field_906.prefix.getValue())) {
            ChatUtil.method_425(var2);
         }

         this.current = (this.current + 1) % this.field_1831.size();
         this.field_1832.reset();
         if (this.autoOff.getValue()) {
            this.method_68();
         }
      }
   }

   public void method_142() {
      try {
         Path var1 = FileUtil.method_2(FileManager.field_4283.resolve(this.path.getValue()), new String[]{".txt"});
         if (!var1.toFile().exists()) {
            throw new RuntimeException("Invalid spammer file path");
         }

         this.current = 0;
         this.field_1831.clear();
         this.field_1831.addAll(FileUtils.readLines(var1.toFile(), StandardCharsets.UTF_8));
      } catch (Exception var2) {
         if (!this.method_535()) {
            ChatUtil.method_2(
               Component.literal("Failed to open spammer file").withStyle(var0 -> var0.withColor(ChatFormatting.RED)),
               ChatUtil.method_38(var2.toString().hashCode()),
               Priority.LOW
            );
         }
      }
   }
}
