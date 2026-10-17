package me.mioclient.module.misc;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.Priority;
import me.mioclient.event.ChatReceiveEvent;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.CommandManager;
import me.mioclient.internal.ChatUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.multiplayer.chat.GuiMessageTag;
import net.minecraft.network.chat.Component;
import me.mioclient.nick.Settings;

public class ChatFilterModule extends Module {
   public Setting<Boolean> caseSensitive;
   public Setting<Boolean> ignoreSelf;
   public String field_2115;

   public ChatFilterModule() {
      super("ChatFilter", "Filters chat messages based on your filters.", Category.MISC);
      Settings.initialize(this);
      this.field_2115 = null;
   }

   @Override
   public void onEnable() {
      if (Hub.field_2627.isEmpty()) {
         String var1 = "You don't have any chat filters set. Use the \"%schatfilter add <id> <filter>\" command to add new filters."
            .formatted(CommandManager.method_927());
         ChatUtil.method_2(Component.nullToEmpty(var1), ChatUtil.method_2(this), Priority.LOW);
      }

      this.field_2115 = null;
   }

   @Subscribe(
      method_800 = 200
   )
   public void method_9(ChatMessageEvent var1) {
      if (var1.method_213() == PreType.PRE && var1.method_1033() != null) {
         GuiMessageTag var2 = var1.method_1036();
         if (var2 == null || var2 == GuiMessageTag.system() || var2 == GuiMessageTag.systemSinglePlayer() || var2 == GuiMessageTag.chatNotSecure()) {
            String var3 = var1.method_1033().getString();
            if (var3 != null) {
               if (this.ignoreSelf.getValue() && this.field_2115 != null && var3.contains(this.field_2115)) {
                  this.field_2115 = null;
                  return;
               }

               if (!Hub.field_2627.isEmpty() && Hub.field_2627.method_82(var3)) {
                  var1.method_463();
               }
            }
         }
      }
   }

   @Subscribe
   public void method_5(ChatReceiveEvent var1) {
      if (!var1.method_464() && var1.method_219() != null && this.ignoreSelf.getValue()) {
         this.field_2115 = var1.method_219();
      }
   }
}
