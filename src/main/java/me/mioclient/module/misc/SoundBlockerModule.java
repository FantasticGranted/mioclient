package me.mioclient.module.misc;

import java.util.Set;
import me.mioclient.event.SoundPlayEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.sounds.SoundEvent;
import me.mioclient.nick.Settings;

public class SoundBlockerModule extends Module {
   public Setting<Set<SoundEvent>> sounds;

   public SoundBlockerModule() {
      super("SoundBlocker", "Blocks certain sounds from being played.", Category.MISC);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(SoundPlayEvent var1) {
      for (SoundEvent var3 : this.sounds.getValue()) {
         if (var3.location().equals(var1.method_863().getIdentifier())) {
            var1.method_463();
            break;
         }
      }
   }
}
