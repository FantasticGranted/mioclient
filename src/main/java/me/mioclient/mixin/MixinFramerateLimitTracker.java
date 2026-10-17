package me.mioclient.mixin;

import com.mojang.blaze3d.platform.FramerateLimitTracker;
import me.mioclient.Hub;
import me.mioclient.internal.ClickGui;
import me.mioclient.module.misc.UnfocusedFPSModule;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// TODO26.1: Minecraft.getFramerateLimit()/Window.getFramerateLimit() were replaced by FramerateLimitTracker.
@Mixin({FramerateLimitTracker.class})
public class MixinFramerateLimitTracker {
   private static UnfocusedFPSModule unfocusedfps = Hub.field_2595.method_78(UnfocusedFPSModule.class);

   public MixinFramerateLimitTracker() {
      super();
   }

   @Inject(
      method = {"getFramerateLimit"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void getFramerateLimit(CallbackInfoReturnable<Integer> var1) {
      Minecraft mcx = Minecraft.getInstance();
      boolean active = mcx.isWindowActive();
      if (unfocusedfps.isToggled() && !active) {
         if (unfocusedfps.fPS.method_468()) {
            var1.setReturnValue(unfocusedfps.method_681());
         } else {
            var1.setReturnValue(unfocusedfps.fPS.getValue());
         }
      } else if (mcx.screen instanceof ClickGui && active) {
         var1.setReturnValue(mcx.options.framerateLimit().get());
      }
   }
}
