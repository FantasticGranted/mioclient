package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.MovementInputPostEvent;
import me.mioclient.mixin.ducks.DuckKeyBinding;
import me.mioclient.module.movement.NoSlowModule;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.KeyboardInput;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({KeyboardInput.class})
public class MixinKeyboardInput extends ClientInput {
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);
   @Shadow
   @Final
   private Options options;

   public MixinKeyboardInput() {
      super();
   }

    @Inject(
       method = {"tick"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private void tickHookPre(CallbackInfo var1) {
       boolean var2 = this.options.keyShift.isDown();
       MovementInputPostEvent var3 = new MovementInputPostEvent((KeyboardInput)(Object)this, var2 ? 0.3F : -1.0F);
       MinecraftAccess.lookups.method_36(var3);
       if (var3.method_464()) {
          MovementInputEvent var4 = new MovementInputEvent(var3.method_276(), var3.method_277());
          MinecraftAccess.lookups.method_36(var4);
          var1.cancel();
       }
    }

    @Inject(
       method = {"tick"},
       at = {@At("TAIL")},
       cancellable = true
    )
    private void tickHook(CallbackInfo var1) {
       boolean var2 = this.options.keyShift.isDown();
       MovementInputEvent var3 = new MovementInputEvent((KeyboardInput)(Object)this, var2 ? 0.3F : -1.0F);
       MinecraftAccess.lookups.method_36(var3);
    }

   @Redirect(
      method = {"tick"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/KeyMapping;isDown()Z"
      ),
      require = 0
   )
   private boolean tickHook2(KeyMapping var1) {
      return noslow.isToggled() && noslow.method_569() && noslow.guiMove.getValue() && var1 != this.options.keyShift
         ? GLFW.glfwGetKey(Minecraft.getInstance().getWindow().handle(), ((DuckKeyBinding)(Object)var1).getKey().getValue()) == 1
         : var1.isDown();
   }
}
