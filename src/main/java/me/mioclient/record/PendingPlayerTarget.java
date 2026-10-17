package me.mioclient.record;

import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.world.entity.player.Player;

/** A player tracked with a timestamp and a cancel flag; expires after 300 ms. */
public final class PendingPlayerTarget {
   public final Player player;
   public final long time;
   public final AtomicBoolean cancelled;

   public PendingPlayerTarget(Player var1, long var2, AtomicBoolean var4) {
      super();
      this.player = var1;
      this.time = var2;
      this.cancelled = var4;
   }

   public boolean isExpired() {
      return System.currentTimeMillis() - this.time >= 300L || this.cancelled.get();
   }

   public Player getPlayer() {
      return this.player;
   }

   public long getTime() {
      return this.time;
   }

   public AtomicBoolean getCancelled() {
      return this.cancelled;
   }
}
