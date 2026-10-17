package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.internal.Class_0152;
import me.mioclient.module.client.UIModule;
import me.mioclient.module.exploit.FakeVanillaModule;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.Checkbox.Builder;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.multiplayer.JoinMultiplayerScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({JoinMultiplayerScreen.class})
public class MixinMultiplayerScreen extends Screen {
   private static final FakeVanillaModule fakevanilla = Hub.field_2595.method_78(FakeVanillaModule.class);
   private static final UIModule clickgui = Hub.field_2595.method_78(UIModule.class);

   protected MixinMultiplayerScreen(Component var1) {
      super(var1);
   }

   @Inject(
      method = {"init"},
      at = {@At("TAIL")}
   )
   private void initHook(CallbackInfo var1) {
      if (!Class_0152.field_442) {
         Builder var2 = Checkbox.builder(Component.nullToEmpty("Vanilla"), this.font)
            .pos(2, 6)
            .selected(fakevanilla.isToggled())
            .onValueChange((var0, var1x) -> fakevanilla.method_38(var1x));
         this.addRenderableWidget(var2.build());
      }
   }

   @Inject(
      method = {"keyPressed"},
      at = {@At("HEAD")},
      cancellable = true
   )
    private void keyPressed(KeyEvent var1, CallbackInfoReturnable<Boolean> var2) {
       if (!(this.getFocused() instanceof EditBox)) {
          if (var1.key() == clickgui.getKeybind().method_38()) {
             var2.setReturnValue(false);
             var2.cancel();
             clickgui.enable();
             Hub.method_755().method_1002();
          }
       }
    }
}
