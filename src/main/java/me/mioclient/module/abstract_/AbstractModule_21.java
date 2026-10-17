package me.mioclient.module.abstract_;

import java.awt.Color;
import me.mioclient.enum_.ColorBlindMode;
import me.mioclient.module.Category;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class AbstractModule_21 extends AbstractModule_41 {
   public Setting<Color> friendColor;
   public Setting<Color> enemyColor;
   public Setting<Color> themeColor;
   public Setting<ColorBlindMode> scheme;

   public AbstractModule_21() {
      super("Colors", "Manages the client's color system.", Category.CLIENT);
      Settings.initialize(this);
   }
}
