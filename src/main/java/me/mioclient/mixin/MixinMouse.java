package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.MouseButtonEvent;
import me.mioclient.event.MouseUpdateEvent;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.player.LocalPlayer;
import me.mioclient.event.MouseScrollEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({MouseHandler.class})
public class MixinMouse {
   public MixinMouse() {
      super();
   }

   @Inject(
      method = {"onButton"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/KeyMapping;click(Lcom/mojang/blaze3d/platform/InputConstants$Key;)V"
      )}
   )
   private void onMouseButtonHook(long var1, MouseButtonInfo var3, int var4, CallbackInfo var5) {
      MouseButtonEvent var6 = new MouseButtonEvent(var3.button(), true);
      MinecraftAccess.lookups.method_36(var6);
   }

   @WrapWithCondition(
      method = {"turnPlayer"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;turn(DD)V"
      )}
   )
   private boolean updateMouseHook(LocalPlayer var1, double var2, double var4) {
      MouseUpdateEvent var6 = new MouseUpdateEvent(var2, var4);
      MinecraftAccess.lookups.method_36(var6);
      return !var6.method_464();
   }

   @Inject(
      method = {"onScroll"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isSpectator()Z",
         shift = Shift.BEFORE
      )},
      cancellable = true
   )
   private void onMouseScroll(long var1, double var3, double var5, CallbackInfo var7) {
      MouseScrollEvent var8 = new MouseScrollEvent(var3, var5);
      MinecraftAccess.lookups.method_36(var8);
      if (var8.method_464()) {
         var7.cancel();
      }
   }
}
