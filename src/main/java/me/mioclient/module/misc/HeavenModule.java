package me.mioclient.module.misc;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.phys.AABB;

public class HeavenModule extends Module {
   public HeavenModule() {
      super("Heaven", "Brings you to heaven after you die.", Category.MISC);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (mc.screen instanceof DeathScreen) {
         if (mc.player.verticalCollision) {
            mc.player.setBoundingBox(new AABB(0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
         }

         mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, 3.0));
      }
   }
}
