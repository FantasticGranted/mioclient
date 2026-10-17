package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.Class_0152;
import me.mioclient.module.client.HUDModule;
import me.mioclient.module.client.UIModule;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({TitleScreen.class})
public class MixinTitleScreen extends Screen implements MinecraftAccess {
   private static HUDModule h = Hub.field_2595.method_78(HUDModule.class);
   private static UIModule c = Hub.field_2595.method_78(UIModule.class);

   protected MixinTitleScreen(Component var1) {
      super(var1);
   }

   @Inject(
      method = {"extractRenderState"},
      at = {@At("TAIL")}
   )
   private void renderHook(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (!Class_0152.field_442) {
         var1.text(
            mc.font,
            "Mio " + ChatFormatting.WHITE + "v2 " + ChatFormatting.GRAY + "(patch " + "02/03/2025 22:47".substring(0, 10) + ")",
            2,
            2,
            h.fade.getValue().method_2(h, 0.0F).getRGB()
         );
      }
   }

   public boolean keyPressed(KeyEvent var1) {
      if (!c.getKeybind().method_39() && c.getKeybind().method_38() == var1.key()) {
         c.enable();
         Hub.method_755().method_1002();
      }

      return true;
   }
}
