package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

/** A BlockPos+Direction placement aimed at a PlayerTarget; keeps whichever target has greater range. */
public final class TargetedPlacement {
   public final BlockPos pos;
   public final Direction direction;
   public final PlayerTarget target;

   public TargetedPlacement(BlockPos var1, Direction var2, PlayerTarget var3) {
      super();
      this.pos = var1;
      this.direction = var2;
      this.target = var3;
   }

   public TargetedPlacement method_2(TargetedPlacement var1) {
      if (var1 == null) {
         return this;
      } else {
         double var2 = var1.target.method_445();
         double var4 = this.target.method_445();
         return var2 > var4 ? var1 : this;
      }
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Direction getDirection() {
      return this.direction;
   }

   public PlayerTarget getTarget() {
      return this.target;
   }
}
