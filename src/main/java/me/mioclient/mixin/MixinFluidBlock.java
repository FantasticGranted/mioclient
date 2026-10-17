package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.exploit.ReachModule;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LiquidBlock.class})
public class MixinFluidBlock {
   private static ReachModule reach = Hub.field_2595.method_78(ReachModule.class);

   public MixinFluidBlock() {
      super();
   }

   @Inject(
      method = {"getShape"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getOutlineShapeHook(BlockState var1, BlockGetter var2, BlockPos var3, CollisionContext var4, CallbackInfoReturnable<VoxelShape> var5) {
      Minecraft var6 = Minecraft.getInstance();
      if (reach.isToggled()
         && reach.liquidPlace.getValue()
         && (var6.player.getMainHandItem().getItem() instanceof BlockItem || var6.player.getOffhandItem().getItem() instanceof BlockItem)) {
         boolean var7 = false;

         for (Direction var11 : Direction.values()) {
            if (var2.getBlockState(var3.relative(var11)).isAir()) {
               var7 = true;
               break;
            }
         }

         double var12 = Minecraft.getInstance().gameRenderer.getMainCamera().position().distanceToSqr(var3.getCenter());
         if (var12 < (double)(reach.modifier.getValue() * reach.modifier.getValue()) && !var7) {
            return;
         }

         var5.setReturnValue(Shapes.block());
         var5.cancel();
      }
   }
}
