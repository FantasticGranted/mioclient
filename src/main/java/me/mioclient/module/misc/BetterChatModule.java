package me.mioclient.module.misc;

import java.awt.Color;
import java.util.Date;
import java.util.regex.Pattern;
import me.mioclient.Hub;
import me.mioclient.enum_.TimeFormat;
import me.mioclient.enum_.Orientation;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.ChatReceiveEvent;
import me.mioclient.event.Event_26;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.ToggleAnimation;
import me.mioclient.internal.ChatUtil;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.protocol.game.ServerboundChatCommandPacket;
import net.minecraft.network.protocol.game.ServerboundChatPacket;
import net.minecraft.util.Mth;
import me.mioclient.nick.Settings;

public class BetterChatModule extends Module {
   public static final Pattern field_3920 = Pattern.compile("^\\d+$");
   public static boolean field_3921 = false;
   public static boolean field_3922 = false;
   public static BetterChatModule field_3923;
   public static PMSoundModule field_3924 = Hub.field_2595.method_78(PMSoundModule.class);
   public Setting<Boolean> timestamps;
   public Setting<Boolean> onlyServer;
   public Setting<TimeFormat> format;
   public Setting<Color> textColor;
   public Setting<Color> bracketsColor;
   public Setting<Boolean> rainbow;
   public Setting<Integer> rainbowAmount;
   public Setting<String> leftBracket;
   public Setting<String> rightBracket;
   public Setting<Boolean> mentionSound;
   public Setting<SoundId> soundMode;
   public Setting<Float> volume;
   public Setting<Boolean> animation;
   public Setting<Orientation> type;
   public Setting<Float> speed;
   public Setting<Integer> alpha;
   public Setting<Boolean> always;
   public Setting<Boolean> customSuffix;
   public Setting<String> suffix;
   public Setting<Boolean> highlight;
   public Setting<Color> color;
   public Setting<Boolean> longChatHistory;
   public Setting<Boolean> noReset;
   public final ToggleAnimation field_3948;
   public boolean field_3949;

   public BetterChatModule() {
      super("BetterChat", "Allows you to customize your chat as you please.", Category.MISC);
      Settings.initialize(this);
      this.field_3948 = new ToggleAnimation(this.speed::getValue, true);
      this.alpha.method_2("None", SettingMode.MIN);
      field_3923 = this;
      this.setDrawn(false);
      this.color.method_31("HighlightColor");
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (!var1.method_464()) {
         if (var1.method_127() instanceof ServerboundChatPacket || var1.method_127() instanceof ServerboundChatCommandPacket) {
            this.field_3949 = true;
         }
      }
   }

   @Subscribe
   public void method_9(ChatMessageEvent var1) {
      if (var1.method_213() == PreType.PRE) {
         this.method_7(var1);
         this.method_5(var1);
         this.method_1091();
         this.field_3949 = false;
      }
   }

   @Subscribe
   public void method_5(ChatReceiveEvent var1) {
      String var2 = var1.method_219();
      if (!field_3920.matcher(var2).matches()) {
         if (this.customSuffix.getValue()) {
            String var3 = new PlaceholderText().method_2(this.suffix.getValue()).method_9(" \u0001");
            int var4 = Mth.clamp(256 - var2.length(), 0, var3.length());
            var1.method_220(new PlaceholderText().method_2(var3.substring(0, var4)).method_2((Object)var2).method_9("\u0001\u0001"));
         }
      }
   }

   @Subscribe
   public void method_2(Event_26 var1) {
      this.field_3948.method_3(false);
      this.field_3948.method_45();
   }

   public void method_5(ChatMessageEvent var1) {
      if (this.timestamps.getValue() && (this.method_2(var1.method_1036()) || !this.onlyServer.getValue())) {
         MutableComponent var2 = Component.empty()
            .append(Component.literal(this.leftBracket.getValue()).withStyle(var1x -> var1x.withColor(this.bracketsColor.getValue().hashCode())))
            .append(Component.literal(this.format.getValue().method_2(new Date())).withStyle(var1x -> var1x.withColor(this.textColor.getValue().hashCode())))
            .append(Component.literal(this.rightBracket.getValue()).withStyle(var1x -> var1x.withColor(this.bracketsColor.getValue().hashCode())))
            .append(" ");
         if (this.rainbow.getValue()) {
            var2 = Component.empty().append(ChatUtil.method_2(var2.getString(), () -> this.rainbowAmount.getValue() * 10, this.textColor.getValue()));
         }

         var1.method_9(var2.append(var1.method_1033()));
      }
   }

   public void method_7(ChatMessageEvent var1) {
      if (this.mentionSound.getValue()) {
         String var2 = var1.method_1033().getString().toLowerCase();
         if (!field_3924.isToggled() || !ChatUtil.method_107(var2)) {
            if (!this.field_3949 && var2.contains(mc.getUser().getName().toLowerCase())) {
               Hub.field_2606.method_2(this.soundMode.getValue()).method_230(this.volume.getValue());
            }
         }
      }
   }

   public void method_1091() {
      if (this.method_1092() && this.type.getValue() == Orientation.BOUNCE) {
         this.field_3948.method_36(true);
      }
   }

   public boolean method_2(GuiMessageTag var1) {
      return var1 == GuiMessageTag.system() || var1 == GuiMessageTag.systemSinglePlayer() || var1 == GuiMessageTag.chatNotSecure();
   }

   public boolean method_1092() {
      return this.isToggled() && this.animation.getValue();
   }

   public static BetterChatModule method_1093() {
      return field_3923;
   }
}
