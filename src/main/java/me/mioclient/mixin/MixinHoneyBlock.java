package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.movement.NoSlowModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.HoneyBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({HoneyBlock.class})
public class MixinHoneyBlock {
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);

   public MixinHoneyBlock() {
      super();
   }

   @Shadow
   private boolean isSlidingDown(BlockPos var1, Entity var2) {
      return false;
   }

   @Redirect(
      method = {"entityInside"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/block/HoneyBlock;isSlidingDown(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/entity/Entity;)Z"
      )
   )
   private boolean onEntityCollisionHook(HoneyBlock var1, BlockPos var2, Entity var3) {
      return noslow.isToggled() && noslow.honey.getValue() ? false : this.isSlidingDown(var2, var3);
   }
}
