package me.mioclient.module.misc;

import java.util.concurrent.ThreadLocalRandom;
import me.mioclient.enum_.DeathTextMode;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.chat.Component;
import me.mioclient.nick.Settings;

public class CustomDeathTextModule extends Module {
   public Setting<DeathTextMode> mode;
   public Setting<String> message;
   public static final String[] field_495 = new String[]{
      "YOU USELESS SHITSTAIN",
      "GAME OVER MOTHERFUCKER! YOU SUCK!",
      "HOW THE HELL ARE YOU SO BAD AT THIS GAME?",
      "HOW DO YOU FUCK UP SO BADLY JESUS FUCKING CHRIST?",
      "YOU DIED! CONGRATS FUCKFACE",
      "OH MY GOD YOU SUCK! AUTO-UNINSTALLING",
      "HOLY FUCKING SHIT SERIOUSLY",
      "YOU FUCKING RETARD!",
      "YOU DENSE FUCK"
   };

   public CustomDeathTextModule() {
      super("CustomDeathText", "Displays a custom message on your death screen.", Category.MISC);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   public Component method_210() {
      return (Component)(this.mode.getValue() == DeathTextMode.CUSTOM
         ? Component.nullToEmpty(this.message.getValue().trim())
         : Component.literal(field_495[ThreadLocalRandom.current().nextInt(field_495.length)]));
   }
}
