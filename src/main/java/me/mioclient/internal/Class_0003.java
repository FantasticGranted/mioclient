package me.mioclient.internal;

import me.mioclient.record.DirectionalBox;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

public class Class_0003 extends Class_0088 {
   public Class_0003() {
      super();
   }

   @Override
   public boolean method_2(BlockPos var1, Player var2, DirectionalBox var3) {
      return BoxUtil.method_4(var2).intersects(var3.getBox().move(var1));
   }
}
