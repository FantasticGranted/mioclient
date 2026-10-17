package me.mioclient.record;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;

/** A 512-block region coordinate (block/chunk position shifted right by REGION_SHIFT). */
public final class RegionPos {
   public final int x;
   public final int z;
   public static final int REGION_SHIFT = 9;

   public RegionPos(int var1, int var2) {
      super();
      this.x = var1;
      this.z = var2;
   }

   public static RegionPos method_9(ChunkPos var0) {
      return new RegionPos(var0.getMinBlockX() >> 9, var0.getMinBlockZ() >> 9);
   }

   public static RegionPos method_535(BlockPos var0) {
      return new RegionPos(var0.getX() >> 9, var0.getZ() >> 9);
   }

   public RegionPos getId(int var1, int var2) {
      return new RegionPos(this.getX() + var1, this.getZ() + var2);
   }

   @Override
   public int hashCode() {
      long var1 = 17L;
      var1 = 31L * var1 + (long)this.x;
      var1 = 31L * var1 + (long)this.z;
      return (int)var1;
   }

   public int getX() {
      return this.x;
   }

   public int getZ() {
      return this.z;
   }
}
