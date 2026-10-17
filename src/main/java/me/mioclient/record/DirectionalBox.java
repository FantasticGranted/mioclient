package me.mioclient.record;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

/** A Direction paired with a Box (built from a face and a VoxelShape's bounds). */
public final class DirectionalBox {
   public final Direction direction;
   public final AABB box;

   public DirectionalBox(Direction var1, AABB var2) {
      super();
      this.direction = var1;
      this.box = var2;
   }

   public static DirectionalBox of(Direction var0, VoxelShape var1) {
      return new DirectionalBox(var0.getOpposite(), var1.bounds());
   }

   public Direction getDirection() {
      return this.direction;
   }

   public AABB getBox() {
      return this.box;
   }
}
