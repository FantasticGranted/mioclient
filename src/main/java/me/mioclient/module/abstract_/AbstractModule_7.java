package me.mioclient.module.abstract_;

import me.mioclient.Hub;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.TextHudElement;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.Setting;
import me.mioclient.setting.StringSetting;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

public class AbstractModule_7 extends AbstractModule_26 {
   public final Setting<Boolean> versionColor = this.add(new BooleanSetting("VersionColor", true));
   public final Setting<String> text = this.add(new StringSetting("Text", "Mio"));

   public AbstractModule_7() {
      super("Watermark");
      HudTextElement var1 = new HudTextElement(
         () -> {
            String var1x = this.method_61();
            return Component.literal(
               new PlaceholderText()
                  .method_2((Object)var1x)
                  .method_2(String.valueOf(this.versionColor.getValue() ? ChatFormatting.WHITE : ""))
                  .method_2(this.text.getValue().trim())
                  .method_9("\u0001\u0001\u0001")
            );
         },
         () -> true
      );
      this.method_2(new TextHudElement(this, var1));
      this.method_14().method_2(ScreenCorner.TOP_LEFT);
      this.method_38(true);
   }

   public String method_61() {
      return Hub.field_2609.method_802() == 62 ? " v2.1.7+" : " v2.1.7";
   }
}
