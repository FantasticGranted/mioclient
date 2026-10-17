package me.mioclient.internal;

import java.util.List;
import me.mioclient.api.PhaseBoxProvider;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.combat.AntiPhaseModule;
import me.mioclient.record.DirectionalBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.record.BlockFaceBox;

public class Class_0088 implements MinecraftAccess, PhaseBoxProvider {
   private static final VoxelShape EAST_SHAPE = Block.box(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
   private static final VoxelShape WEST_SHAPE = Block.box(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
   private static final VoxelShape SOUTH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
   private static final VoxelShape NORTH_SHAPE = Block.box(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
   public static final List<DirectionalBox> field_297 = List.of(
      DirectionalBox.of(Direction.EAST, EAST_SHAPE),
      DirectionalBox.of(Direction.WEST, WEST_SHAPE),
      DirectionalBox.of(Direction.NORTH, NORTH_SHAPE),
      DirectionalBox.of(Direction.SOUTH, SOUTH_SHAPE)
   );

   public Class_0088() {
      super();
   }

   @Override
   public BlockFaceBox method_2(AntiPhaseModule var1, Player var2) {
      BlockPos var3 = PlayerUtil.method_9(var2);
      Direction var4 = null;
      AABB var5 = null;
      float var6 = 999F;
      if (!BlockUtil.method_14(var3)) {
         return null;
      } else {
         for (DirectionalBox var8 : field_297) {
            Direction var9 = var8.getDirection();
            BlockPos var10 = var3.relative(var9);
            if (!this.method_2(var3, var2, var8) && (!var1.strictDirection.getValue() || !this.method_7(var10, var9))) {
               float var11 = (float)var2.position().distanceToSqr(var10.getCenter());
               if (var11 < var6) {
                  var5 = var8.getBox();
                  var4 = var9;
                  var6 = var11;
               }
            }
         }

         return var4 == null ? null : new BlockFaceBox(var3.relative(var4), var4.getOpposite(), var5.move(var3));
      }
   }

   public boolean method_2(BlockPos var1, Player var2, DirectionalBox var3) {
      Direction var4 = var3.getDirection();
      BlockPos var5 = var1.relative(var4);
      return BoxUtil.method_4(var2).intersects(var3.getBox().move(var1)) || mc.level.getBlockState(var5).canBeReplaced();
   }

   public boolean method_7(BlockPos var1, Direction var2) {
      List var3 = InteractionUtil.method_39(var1);
      return !var3.contains(var2.getOpposite());
   }
}
