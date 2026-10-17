package me.mioclient.module.misc;

import me.mioclient.enum_.CombatVersion;
import me.mioclient.enum_.SwingHand;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class SwingModule extends Module {
   public Setting<SwingHand> hand;
   public Setting<CombatVersion> type;
   public Setting<Double> speed;

   public SwingModule() {
      super("Swing", "Changes your swinging hand.", Category.MISC);
      Settings.initialize(this);
   }
}
