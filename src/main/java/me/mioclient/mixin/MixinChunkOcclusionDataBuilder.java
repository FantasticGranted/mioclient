package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.module.render.XrayModule;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.core.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({VisGraph.class})
public class MixinChunkOcclusionDataBuilder {
   private static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);

   public MixinChunkOcclusionDataBuilder() {
      super();
   }

   @Inject(
      method = {"setOpaque"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void markClosedHook(BlockPos var1, CallbackInfo var2) {
      if (XrayModule.method_844().isToggled() || freecam.isToggled()) {
         var2.cancel();
      }
   }
}
