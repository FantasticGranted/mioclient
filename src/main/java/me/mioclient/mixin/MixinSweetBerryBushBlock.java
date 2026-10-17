package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.movement.NoSlowModule;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({SweetBerryBushBlock.class})
public class MixinSweetBerryBushBlock {
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);

   public MixinSweetBerryBushBlock() {
      super();
   }

   @Redirect(
      method = {"entityInside"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;makeStuckInBlock(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/phys/Vec3;)V"
      )
   )
   private void onEntityCollisionHook(Entity var1, BlockState var2, Vec3 var3) {
      if (!noslow.isToggled() || !noslow.berryBush.getValue()) {
         var1.makeStuckInBlock(var2, var3);
      }
   }
}
