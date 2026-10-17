package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.systems.RenderSystem;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.HudIconMode;
import me.mioclient.event.Event_26;
import me.mioclient.internal.Render2D;
import me.mioclient.internal.RenderUtil;
import me.mioclient.module.abstract_.AbstractModule_38;
import me.mioclient.module.client.HUDModule;
import me.mioclient.module.render.CrosshairModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.AttackIndicatorStatus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.world.scores.Objective;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import com.mojang.blaze3d.opengl.GlStateManager;

@Mixin({Gui.class})
public class MixinInGameHud implements MinecraftAccess {
   private static HUDModule hud = Hub.field_2595.method_78(HUDModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static CrosshairModule crosshair = Hub.field_2595.method_78(CrosshairModule.class);
   private static AbstractModule_38 nohitdelay = Hub.field_2595.method_78(AbstractModule_38.class);

   public MixinInGameHud() {
      super();
   }

   @Inject(
      method = {"extractTabList"},
      at = {@At("HEAD")}
   )
   public void render(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      hud.method_340(mc.screen instanceof ChatScreen ? 12.0F : 0.0F);
      if (!Minecraft.getInstance().gui.getDebugOverlay().showDebugScreen() && !Minecraft.getInstance().options.hideGui) {

         var1.fill(-1, -1, 0, 0, 0);
         GlStateManager._disableDepthTest();
         GlStateManager._enableBlend();
         GlStateManager._blendFuncSeparate(770, 771, 770, 771);
         GlStateManager._disableCull();
         GL11.glEnable(2848);
         Event_26 var4 = Event_26.method_2(RenderUtil.mioPose(var1.pose()), var1, RenderUtil.method_776());
         lookups.method_36(var4);
         Render2D.method_474();
         GlStateManager._enableDepthTest();
         GL11.glDisable(2848);
      }
   }

   @Inject(
      method = {"extractSelectedItemName(Lnet/minecraft/client/gui/GuiGraphicsExtractor;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderHeldItemTooltipHook(GuiGraphicsExtractor var1, CallbackInfo var2) {
      if (norender.isToggled() && norender.heldTooltips.getValue()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"extractEffects"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void renderStatusEffectOverlayHook(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (hud.isToggled() && hud.icons.getValue() == HudIconMode.HIDE) {
         var3.cancel();
      }
   }

   @Inject(
      method = {"extractCrosshair"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onRenderCrosshair(GuiGraphicsExtractor var1, DeltaTracker var2, CallbackInfo var3) {
      if (crosshair.isToggled()) {
         var3.cancel();
      }
   }

    @ModifyExpressionValue(
       method = {"extractCrosshair"},
       at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;",
          ordinal = 0
       )}
    )
    public <T> Object renderAttackIndicatorHook(T var1) {
      return nohitdelay.isToggled() ? AttackIndicatorStatus.OFF : var1;
   }

   @Inject(
       method = {"displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/scores/Objective;)V"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private void renderScoreboardSidebarHook(GuiGraphicsExtractor var1, Objective var2, CallbackInfo var3) {
      if (norender.isToggled() && norender.scoreBoard.getValue()) {
         var3.cancel();
      }

      var1.pose().pushMatrix();
      var1.pose().translate((float)(-Math.max(hud.method_341() - 1, 0)), 0.0F);
   }

   @Inject(
       method = {"displayScoreboardSidebar(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/scores/Objective;)V"},
       at = {@At("TAIL")},
       cancellable = true
    )
    private void nigger(GuiGraphicsExtractor var1, Objective var2, CallbackInfo var3) {
      var1.pose().popMatrix();
   }

   @ModifyArgs(
      method = {"extractCameraOverlays"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/Gui;extractTextureOverlay(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/resources/Identifier;F)V",
         ordinal = 0
      )
   )
   private void renderOverlayHook(Args var1) {
      if (norender.isToggled() && norender.blindness.getValue()) {
         var1.set(2, 0.0F);
      }
   }
}
