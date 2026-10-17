package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.movement.NoSlowModule;
import me.mioclient.module.render.XrayModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Block.class})
public class MixinBlock {
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);

   public MixinBlock() {
      super();
   }

    @Inject(
       method = {"shouldRenderFace"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private static void onShouldDrawHook(BlockState var0, BlockState var1, Direction var2, CallbackInfoReturnable<Boolean> var3) {
       if (XrayModule.method_844().isToggled()) {
          var3.cancel();
          var3.setReturnValue(
             XrayModule.method_844().whitelist.getValue().contains(var0.getBlock())
                && (!XrayModule.method_844().bypass.getValue() || !var1.canOcclude())
          );
       }
    }

   @Inject(
      method = {"getFriction"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getSlipperinessHook(CallbackInfoReturnable<Float> var1) {
      if (noslow.isToggled()) {
         Block var2 = (Block)(Object)this;
         if (noslow.field_1695.getValue() && (var2 == Blocks.ICE || var2 == Blocks.PACKED_ICE || var2 == Blocks.BLUE_ICE)) {
            var1.setReturnValue(0.6F);
         }

         if (noslow.slime.getValue() && var2 == Blocks.SLIME_BLOCK) {
            var1.setReturnValue(0.6F);
         }

         if (noslow.honey.getValue() && var2 == Blocks.HONEY_BLOCK) {
            var1.setReturnValue(0.6F);
         }
      }
   }
}
