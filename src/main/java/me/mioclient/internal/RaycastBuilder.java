package me.mioclient.internal;

import me.mioclient.api.BlockFilter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.Vec3;

/**
 * Fluent builder for a {@link FilteredRaycastContext}: start/end points plus shape type,
 * fluid handling, source entity and a block filter.
 */
public class RaycastBuilder {
   public final Vec3 start;
   public final Vec3 end;
   public Block field_1627 = Block.COLLIDER;
   public Fluid field_1628 = Fluid.NONE;
   public Entity field_581 = Minecraft.getInstance().player;
   public BlockFilter field_1630 = BlockFilter.field_2968;

   public RaycastBuilder(Vec3 var1, Vec3 var2) {
      super();
      this.start = var1;
      this.end = var2;
   }

   public RaycastBuilder method_2(Block var1) {
      this.field_1627 = var1;
      return this;
   }

   public RaycastBuilder method_2(Fluid var1) {
      this.field_1628 = var1;
      return this;
   }

   public RaycastBuilder method_22(Entity var1) {
      this.field_581 = var1;
      return this;
   }

   public RaycastBuilder method_2(BlockFilter var1) {
      this.field_1630 = var1;
      return this;
   }

   public RaycastBuilder method_2(BlockFilter... var1) {
      this.field_1630 = BlockFilter.method_9(var1);
      return this;
   }

   public FilteredRaycastContext method_565() {
      return new FilteredRaycastContext(this.start, this.end, this.field_1627, this.field_1628, this.field_581, this.field_1630);
   }
}
