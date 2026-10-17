package me.mioclient.internal;

import me.mioclient.api.BlockFilter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

/**
 * Immutable raycast parameters (start/end, shape type, fluid handling, shape context and a
 * block filter) — a RaycastContext variant that adds custom block filtering.
 */
public final class FilteredRaycastContext {
   public final Vec3 start;
   public final Vec3 end;
   public final Block field_1627;
   public final Fluid field_1628;
   public final CollisionContext shapeContext;
   public final BlockFilter field_1630;

   public FilteredRaycastContext(Vec3 var1, Vec3 var2, Block var3, Fluid var4, Entity var5, BlockFilter var6) {
      super();
      this.start = var1;
      this.end = var2;
      this.field_1627 = var3;
      this.field_1628 = var4;
      this.shapeContext = CollisionContext.of(var5);
      this.field_1630 = var6;
   }

   public Vec3 method_543() {
      return this.start;
   }

   public Vec3 method_544() {
      return this.end;
   }

   public Block method_545() {
      return this.field_1627;
   }

   public Fluid method_546() {
      return this.field_1628;
   }

   public CollisionContext method_547() {
      return this.shapeContext;
   }

   public BlockFilter method_548() {
      return this.field_1630;
   }

   public VoxelShape method_2(BlockState var1, BlockGetter var2, BlockPos var3) {
      return this.field_1627.get(var1, var2, var3, this.shapeContext);
   }

   public VoxelShape method_2(FluidState var1, BlockGetter var2, BlockPos var3) {
      return this.field_1628.canPick(var1) ? var1.getShape(var2, var3) : Shapes.empty();
   }
}
