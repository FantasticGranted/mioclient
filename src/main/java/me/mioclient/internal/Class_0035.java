package me.mioclient.internal;

import me.mioclient.api.PhaseBoxProvider;
import me.mioclient.module.combat.AntiPhaseModule;
import me.mioclient.record.BlockFaceBox;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;

public class Class_0035 implements PhaseBoxProvider {
   public Class_0035() {
      super();
   }

   @Override
   public BlockFaceBox method_2(AntiPhaseModule var1, Player var2) {
      BlockPos var3 = PlayerUtil.method_9(var2);
      Direction var4 = InteractionUtil.method_9(var3, var1.strictDirection.getValue());
      return var4 != null && BlockUtil.method_14(var3) ? new BlockFaceBox(var3.relative(var4), var4.getOpposite(), this.method_5(var3, var4)) : null;
   }

   public AABB method_5(BlockPos var1, Direction var2) {
      return new AABB(var1);
   }
}
