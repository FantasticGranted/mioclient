package me.mioclient.module.misc;

import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import me.mioclient.nick.Settings;

public class NoPacketKickModule extends Module {
   public Setting<Boolean> message;

   public NoPacketKickModule() {
      super("NoPacketKick", "Cancels broken packets that may cause you getting kicked.", Category.MISC);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   public Component method_38(Throwable var1) {
      return Component.translatable("disconnect.genericReason", new Object[]{new PlaceholderText().method_2(String.valueOf(var1)).method_9("Internal Exception: \u0001")})
         .withStyle(ChatFormatting.RED);
   }
}
