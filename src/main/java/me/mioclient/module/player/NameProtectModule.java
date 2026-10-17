package me.mioclient.module.player;

import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class NameProtectModule extends Module {
   public Setting<String> name;
   public Setting<Boolean> skin;
   public Setting<Boolean> slim;

   public NameProtectModule() {
      super("NameProtect", "Hides your nickname from the curious people.", Category.PLAYER);
      Settings.initialize(this);
   }
}
