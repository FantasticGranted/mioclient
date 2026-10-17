package me.mioclient.enum_;

import java.util.function.Function;
import me.mioclient.api.Nameable;
import net.minecraft.core.BlockPos;
import me.mioclient.api.MinecraftAccess;

/** Ordering strategy for candidate blocks (closest/furthest/top/bottom) via a distance key. */
public enum BlockSort implements Nameable {
   CLOSEST("Closest", var0 -> MinecraftAccess.mc.player.getEyePosition().distanceToSqr(var0.getCenter())),
   FURTHEST("Furthest", var0 -> -MinecraftAccess.mc.player.getEyePosition().distanceToSqr(var0.getCenter())),
   TOP("Top", var0 -> (double)(-var0.getY())),
   BOTTOM("Bottom", var0 -> (double)var0.getY());

   public final String displayName;
   public final Function<BlockPos, Double> field_1223;

    BlockSort(String var3, Function<BlockPos, Double> var4) {
      this.displayName = var3;
      this.field_1223 = var4;
   }

   @Override
   public String getName() {
      return this.displayName;
   }
}
