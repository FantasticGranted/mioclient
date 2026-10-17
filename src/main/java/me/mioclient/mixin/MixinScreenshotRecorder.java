package me.mioclient.mixin;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import java.util.function.Consumer;
import me.mioclient.Hub;
import me.mioclient.module.misc.ExtraScreenshotModule;
import net.minecraft.util.Util;
import net.minecraft.client.Screenshot;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin({Screenshot.class})
public class MixinScreenshotRecorder {
   private static ExtraScreenshotModule screenshot = Hub.field_2595.method_78(ExtraScreenshotModule.class);

   public MixinScreenshotRecorder() {
      super();
   }

   @Redirect(
      method = {"grab(Ljava/io/File;Ljava/lang/String;Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Screenshot;takeScreenshot(Lcom/mojang/blaze3d/pipeline/RenderTarget;ILjava/util/function/Consumer;)V"
      )
   )
   private static void saveScreenshotInner(RenderTarget var0, int var1, Consumer<NativeImage> var2) {
      Screenshot.takeScreenshot(var0, var1, (NativeImage var3) -> {
         if (screenshot.isToggled()) {
            Util.ioPool().execute(() -> screenshot.method_2(var3));
            if (screenshot.method_807()) {
               return;
            }
         }

         var2.accept(var3);
      });
   }

   @ModifyVariable(
      method = {"grab"},
      at = @At("HEAD"),
      ordinal = 0,
      argsOnly = true
   )
   private static Consumer<Component> modifyMessageReceiver(Consumer<Component> var0) {
      return screenshot.isToggled() && !screenshot.method_808() ? (Component var1) -> {
      } : var0;
   }
}
