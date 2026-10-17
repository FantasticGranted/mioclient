package me.mioclient.record;

import me.mioclient.enum_.TrapType;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public final class TrapPlacement {
   public final Player player;
   public final TrapType trapType;
   @Nullable
   public final Direction direction;

   public TrapPlacement(Player var1, TrapType var2, @Nullable Direction var3) {
      super();
      this.player = var1;
      this.trapType = var2;
      this.direction = var3;
   }

   public Player getPlayer() {
      return this.player;
   }

   public TrapType getTrapType() {
      return this.trapType;
   }

   @Nullable
   public Direction getDirection() {
      return this.direction;
   }
}
