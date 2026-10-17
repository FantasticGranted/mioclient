package me.mioclient.record;

import me.mioclient.enum_.RaytraceMode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

/** A raytrace target candidate (position, player, damage, {@link me.mioclient.enum_.RaytraceMode}); {@code method_2} keeps the better of two by mode then damage. */
public final class RaytraceTarget {
   public final BlockPos pos;
   public final Player player;
   public final double damage;
   public final RaytraceMode mode;

   public RaytraceTarget(BlockPos var1, Player var2, double var3, RaytraceMode var5) {
      super();
      this.pos = var1;
      this.player = var2;
      this.damage = var3;
      this.mode = var5;
   }

   public RaytraceTarget method_2(RaytraceTarget var1) {
      if (var1 == null) {
         return this;
      } else if (this.mode.ordinal() < var1.mode.ordinal()) {
         return this;
      } else if (this.mode.ordinal() > var1.mode.ordinal()) {
         return var1;
      } else {
         return var1.damage > this.damage ? var1 : this;
      }
   }

   public BlockPos getPos() {
      return this.pos;
   }

   public Player method_444() {
      return this.player;
   }

   public double method_445() {
      return this.damage;
   }

   public RaytraceMode method_446() {
      return this.mode;
   }
}
