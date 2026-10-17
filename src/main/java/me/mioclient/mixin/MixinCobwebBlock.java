package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.movement.FastWebModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.InsideBlockEffectApplier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WebBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({WebBlock.class})
public class MixinCobwebBlock {
   private static final FastWebModule fastweb = Hub.field_2595.method_78(FastWebModule.class);

   public MixinCobwebBlock() {
      super();
   }

   @Inject(
      method = {"entityInside"},
      at = {@At("HEAD")},
      cancellable = true
   )
    private void onEntityCollisionHook(BlockState var1, Level var2, BlockPos var3, Entity var4, InsideBlockEffectApplier var5, boolean var6, CallbackInfo var7) {
       if (fastweb.method_726()) {
          var7.cancel();
       }
    }
}
