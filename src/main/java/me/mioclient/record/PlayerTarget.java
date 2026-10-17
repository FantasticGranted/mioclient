package me.mioclient.record;

import me.mioclient.Hub;
import me.mioclient.api.ServerSideDeadAccess;
import net.minecraft.world.entity.player.Player;
import me.mioclient.api.MinecraftAccess;

/** A target player and range, with a static validity check (isInvalid). */
public final class PlayerTarget implements MinecraftAccess {
   public final Player player;
   public final double range;

   public PlayerTarget(Player var1, double var2) {
      super();
      this.player = var1;
      this.range = var2;
   }

   public static boolean isInvalid(Player var0, double var1) {
      return var0 == null
         || var0 == mc.player
         || !var0.isAlive()
         || Hub.field_2603.getId(var0)
         || ((ServerSideDeadAccess)var0).isServerSideDead()
         || var0.isSpectator()
         || var0.getAbilities().instabuild
         || (double)mc.player.distanceTo(var0) > var1;
   }

   public Player getPlayer() {
      return this.player;
   }

   public double method_445() {
      return this.range;
   }
}
