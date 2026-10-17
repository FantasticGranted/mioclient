package me.mioclient.mixin;

import me.mioclient.Hub;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.CommandManager;
import me.mioclient.internal.ClickGui;
import me.mioclient.internal.MioFramebuffer;
import me.mioclient.module.client.HUDModule;
import me.mioclient.module.render.BlurModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({Screen.class})
public abstract class MixinScreen {
   private static final NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static final BlurModule blur = Hub.field_2595.method_78(BlurModule.class);
   private static final HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   @Shadow
   protected Minecraft minecraft;
   @Shadow
   public int width;
   @Shadow
   public int height;

   public MixinScreen() {
      super();
   }

   @Shadow
   protected abstract void extractMenuBackground(GuiGraphicsExtractor var1);

   @Inject(
      method = {"clickCommandAction"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private static void handleTextClickHook(LocalPlayer var0, String var1, Screen var2, CallbackInfo var3) {
      if (var0 != null && var0.connection != null && var1 != null && var1.startsWith(CommandManager.method_927())) {
         var0.connection.sendChat(var1);
         var3.cancel();
      }
   }

   @Inject(
      method = {"extractTransparentBackground"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderInGameBackgroundHook(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (norender.method_285()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"extractBackground"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/Screen;extractBlurredBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V",
         shift = Shift.BEFORE
      )},
      cancellable = true
   )
   private void renderBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4, CallbackInfo var5) {
      if (blur.isToggled() && this.minecraft.level != null && this.minecraft.player != null) {
         this.extractMenuBackground(var1);
         var5.cancel();
      } else if (Minecraft.getInstance().screen instanceof ClickGui) {
         float var6 = (float)this.minecraft.options.getMenuBackgroundBlurriness();
         this.extractMenuBackground(var1);
         MioFramebuffer.method_2(() -> var1.fill(0, 0, this.width, this.height, -1), var6);
         var5.cancel();
      }
   }

   @Redirect(
      method = {"extractRenderState"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/Renderable;extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V"
      )
   )
   private void renderHook3(Renderable var1, GuiGraphicsExtractor var2, int var3, int var4, float var5) {
      if ((Object)this instanceof ChatScreen && var1 instanceof EditBox
         && !((double)(hud.method_338() / 13.5F) > Class_0245.field_689)) {
         return;
      }
      var1.extractRenderState(var2, var3, var4, var5);
   }
}
