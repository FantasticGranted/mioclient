package me.mioclient.mixin;

import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.MouseButtonEvent;
import me.mioclient.event.KeyPressEvent;
import me.mioclient.module.misc.BetterChatModule;
import net.minecraft.client.KeyboardHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({KeyboardHandler.class})
public class MixinKeyboard {
   @Shadow
   @Final
   private Minecraft minecraft;

   public MixinKeyboard() {
      super();
   }

   @Inject(
      method = {"keyPress"},
      at = {@At("TAIL")},
      cancellable = true
   )
    private void onKey(long var1, int var3, KeyEvent var4, CallbackInfo var5) {
       if (var4.key() >= 0 && this.minecraft.screen == null && var3 == 1) {
          MouseButtonEvent var6 = new MouseButtonEvent(var4.key(), false);
          MinecraftAccess.lookups.method_36(var6);
          if (var6.method_464()) {
             var5.cancel();
          }
       }
    }

    @Inject(
       method = {"charTyped"},
       at = {@At("HEAD")}
    )
    public void onChar(long var1, CharacterEvent var3, CallbackInfo var4) {
       if (this.minecraft.screen == null && var1 == this.minecraft.getWindow().handle()) {
          KeyPressEvent var5 = new KeyPressEvent(var3.codepoint());
          MinecraftAccess.lookups.method_36(var5);
       }
    }

   @Inject(
      method = {"handleDebugKeys"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent;clearMessages(Z)V",
         shift = Shift.BEFORE
      )}
   )
    private void processF3Pre(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
       BetterChatModule.field_3922 = true;
    }

   @Inject(
      method = {"handleDebugKeys"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ChatComponent;clearMessages(Z)V",
         shift = Shift.AFTER
      )}
   )
    private void processF3Post(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
       BetterChatModule.field_3922 = false;
    }
}
