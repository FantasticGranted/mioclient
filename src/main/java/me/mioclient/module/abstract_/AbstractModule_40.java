package me.mioclient.module.abstract_;

import me.mioclient.enum_.Class_0602;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.TextHudElement;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.Setting;
import me.mioclient.setting.StringSetting;

public class AbstractModule_40 extends AbstractModule_26 {
   public Setting<Class_0602> mode = this.add(new CustomSetting<>("Mode", Class_0602.NAME));
   public final Setting<String> text = this.add(new StringSetting("Text", "Welcome to Mio.", var1 -> this.mode.getValue() == Class_0602.CUSTOM));

   public AbstractModule_40() {
      super("Welcomer");
      this.method_2(new TextHudElement(this, new HudTextElement(() -> this.mode.getValue().method_2(this), () -> true)));
   }
}
