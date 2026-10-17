package me.mioclient.module.misc;

import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class AntiQuitModule extends Module {
   public Setting<Boolean> disconnect;
   public Setting<Boolean> gameClose;

   public AntiQuitModule() {
      super("AntiQuit", "Prevents you from quitting the game/server accidentally.", Category.MISC, "antidisconnect");
      Settings.initialize(this);
   }
}
