package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.movement.NoSlowModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({SlimeBlock.class})
public class MixinSlimeBlock {
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);

   public MixinSlimeBlock() {
      super();
   }

   @Inject(
      method = {"stepOn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSteppedOnHook(Level var1, BlockPos var2, BlockState var3, Entity var4, CallbackInfo var5) {
      if (noslow.isToggled() && noslow.slime.getValue()) {
         var5.cancel();
      }
   }

   @Inject(
      method = {"bounceUp"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void bounceHook(Entity var1, CallbackInfo var2) {
      if (noslow.isToggled() && noslow.slime.getValue() && noslow.noSlimeBounce.getValue()) {
         var2.cancel();
      }
   }
}
