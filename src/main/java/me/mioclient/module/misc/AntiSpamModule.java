package me.mioclient.module.misc;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import me.mioclient.api.VisibleLinesAccess;
import me.mioclient.enum_.PreType;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.Pair;
import me.mioclient.setting.Setting;
import net.minecraft.client.multiplayer.chat.GuiMessage.Line;
import net.minecraft.network.chat.Component;
import me.mioclient.nick.Settings;

public class AntiSpamModule extends Module {
   public static final Pattern field_3856 = Pattern.compile(
      "(https?:\\/\\/)?([\\w\\-])+\\.{1}([a-zA-Z]{2,63})([\\/\\w-]*)*\\/?\\??([^#\\n\\r]*)?#?([^\\n\\r]*)"
   );
   public final Map<String, Pair<Long, Line>> field_3857 = new HashMap<>();
   public Setting<Boolean> indicator;
   public Setting<Boolean> links;
   public String field_3860;

   public AntiSpamModule() {
      super("AntiSpam", "Stacks similar chat messages with each other.", Category.MISC);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Override
   public void onToggle() {
      this.field_3857.clear();
      this.field_3860 = null;
   }

   @Subscribe
   public void method_9(DisconnectEvent var1) {
      this.field_3857.clear();
      this.field_3860 = null;
   }

   @Subscribe
   public void method_9(ChatMessageEvent var1) {
      if (var1.method_213() == PreType.PRE) {
         if (var1.getSignature() != null && var1.getSignature().asByteBuffer().getInt() < 0) {
            return;
         }

         this.field_3860 = var1.method_1033().getString().toLowerCase(Locale.ROOT);
         this.field_3857.computeIfPresent(this.field_3860, (var0, var1x) -> {
            ((VisibleLinesAccess)mc.gui.getChat()).getVisible().remove(var1x.getRight());
            return var1x;
         });
         this.field_3857.putIfAbsent(this.field_3860, new Pair<>(1L, var1.method_1034()));
         if (this.links.getValue() && field_3856.matcher(this.field_3860).find()) {
            var1.method_463();
         }

         long var2 = this.field_3857.get(this.field_3860).getLeft();
         if (var2 > 1L && this.indicator.getValue()) {
            var1.method_9(Component.empty().append(var1.method_1033()).append(Component.literal(new PlaceholderText().method_2(var2).method_9(" [x\u0001]"))));
         }
      } else if (this.field_3860 != null) {
         this.field_3857.computeIfPresent(this.field_3860, (var1x, var2x) -> new Pair<>(var2x.getLeft() + 1L, var1.method_1034()));
      }
   }
}
