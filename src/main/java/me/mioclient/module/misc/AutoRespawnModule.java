package me.mioclient.module.misc;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import net.minecraft.client.gui.screens.DeathScreen;

public class AutoRespawnModule extends Module {
   public final Timer field_2393 = new Timer();

   public AutoRespawnModule() {
      super("AutoRespawn", "Respawns automatically.", Category.MISC);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (mc.screen instanceof DeathScreen && this.field_2393.method_9(150L)) {
         mc.player.respawn();
         this.field_2393.reset();
      }
   }
}
