package me.mioclient.module.misc;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.ChatMessageEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.SoundId;
import me.mioclient.internal.ChatUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class PMSoundModule extends Module {
   public Setting<SoundId> type;
   public Setting<Float> volume;

   public PMSoundModule() {
      super("PMSound", "Plays a sound whenever you get a private message.", Category.MISC);
      Settings.initialize(this);
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_9(ChatMessageEvent var1) {
      if (!var1.method_464()) {
         if (var1.method_213() == PreType.PRE && var1.method_1033() != null && ChatUtil.method_107(var1.method_1033().getString())) {
            mc.executeIfPossible(() -> Hub.field_2606.method_2(this.type.getValue()).method_230(this.volume.getValue()));
         }
      }
   }
}
