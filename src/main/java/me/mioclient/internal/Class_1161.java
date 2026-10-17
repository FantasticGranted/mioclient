package me.mioclient.internal;

import java.util.List;
import me.mioclient.record.DirectionalBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;

public class Class_1161 extends Class_0035 {
   public static final List<DirectionalBox> field_3597 = List.of(
      DirectionalBox.of(
         Direction.EAST,
         Block.box(
            0.0,
            2.0,
            2.0,
            1.0,
            14.0,
            14.0
         )
      ),
      DirectionalBox.of(
         Direction.WEST,
         Block.box(
            15.0,
            2.0,
            2.0,
            16.0,
            14.0,
            14.0
         )
      ),
      DirectionalBox.of(
         Direction.NORTH,
         Block.box(
            2.0,
            2.0,
            0.0,
            14.0,
            14.0,
            1.0
         )
      ),
      DirectionalBox.of(
         Direction.SOUTH,
         Block.box(
            2.0,
            2.0,
            15.0,
            14.0,
            14.0,
            16.0
         )
      ),
      DirectionalBox.of(
         Direction.DOWN,
         Block.box(
            2.0,
            0.0,
            2.0,
            14.0,
            1.0,
            14.0
         )
      ),
      DirectionalBox.of(
         Direction.UP,
         Block.box(
            2.0,
            15.0,
            2.0,
            14.0,
            16.0,
            14.0
         )
      )
   );

   public Class_1161() {
      super();
   }

   @Override
   public AABB method_5(BlockPos var1, Direction var2) {
      for (DirectionalBox var4 : field_3597) {
         if (var4.getDirection() == var2.getOpposite()) {
            return var4.getBox().move(var1);
         }
      }

      return new AABB(var1);
   }
}
