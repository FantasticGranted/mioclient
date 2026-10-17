package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.enum_.BlockSafety;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.BoxRenderer;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.Colors;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.PhaseBlock;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;

public class PhaseESPModule extends Module {
   public Setting<Color> safe;
   public Setting<Color> semiSafe;
   public Setting<Color> unsafe;
   public Setting<Boolean> outline;
   public Setting<Float> alpha;

   public PhaseESPModule() {
      super("PhaseESP", "Highlights safe blocks to phase into.", Category.RENDER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      AABB var2 = mc.player
         .getBoundingBox()
         .setMaxY(mc.player.getBoundingBox().minY)
         .inflate(0.01, 0.0, 0.01);
      if (!InteractionUtil.method_16(mc.player.blockPosition()) && !mc.player.isVisuallySwimming()) {
         BlockPos.betweenClosedStream(var2)
            .filter(this::method_1011)
            .map(var1x -> new PhaseBlock(var1x, this.method_473(var1x)))
            .forEach(
               var2x -> {
                  VoxelShape var3 = mc.level.getBlockState(var2x.pos).getShape(mc.level, var2x.pos);
                  if (var3.isEmpty()) {
                     var3 = Shapes.box(
                        0.0,
                        0.0,
                        0.0,
                        1.0,
                        1.0,
                        1.0
                     );
                  }

                  AABB var4 = var3.bounds().move(var2x.pos);
                  BoxRenderer.method_5(var1.method_10(), var4.setMaxY((double)var2x.pos.getY()), var2x.safety.method_2(this));
                  if (this.outline.getValue()) {
                     BoxRenderer.method_2(
                        var1.method_10(),
                        var4.setMaxY((double)var2x.pos.getY()),
                        Colors.method_2(var2x.safety.method_2(this), this.alpha.getValue()),
                        1F
                     );
                  }
               }
            );
      }
   }

   public BlockSafety method_473(BlockPos var1) {
      boolean var2 = mc.level.getBlockState(var1).getBlock().getExplosionResistance() >= 6e+02F;
      if (!InteractionUtil.method_16(var1.below())
         || !var2
         || mc.level.getBlockState(var1.below()).getBlock().getExplosionResistance() < 6e+02F) {
         return BlockSafety.UNSAFE;
      } else {
         return !this.method_287(var1) && !this.method_287(var1.below()) ? BlockSafety.SAFE : BlockSafety.SEMI;
      }
   }

   public boolean method_287(BlockPos var1) {
      return InteractionUtil.getId(var1) != Blocks.BEDROCK;
   }

   public boolean method_1011(BlockPos var1) {
      BlockState var2 = mc.level.getBlockState(var1);
      return var2.getBlock().getExplosionResistance() >= 6e+02F && InteractionUtil.method_16(var1) && !var2.canBeReplaced();
   }
}
