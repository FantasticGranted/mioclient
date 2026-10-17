package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.platform.Window;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.FrameRenderEvent;
import me.mioclient.event.DisconnectEvent;
import me.mioclient.event.Event_21;
import me.mioclient.event.ScreenOpenEvent;
import me.mioclient.event.ScreenOpenPostEvent;
import me.mioclient.internal.CloseConfirmScreen;
import me.mioclient.internal.ClickGui;
import me.mioclient.module.abstract_.AbstractModule_38;
import me.mioclient.module.client.UIModule;
import me.mioclient.module.combat.ArrowsModule;
import me.mioclient.module.combat.AutoClickerModule;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.exploit.MultiTaskModule;
import me.mioclient.module.misc.AntiQuitModule;
import me.mioclient.module.misc.UnfocusedFPSModule;
import me.mioclient.module.player.AutoEatModule;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.module.render.FreeLookModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({Minecraft.class})
public class MixinMinecraft {
   private static UIModule clickgui = Hub.field_2595.method_78(UIModule.class);
   private static MultiTaskModule multitask = Hub.field_2595.method_78(MultiTaskModule.class);
   private static AutoEatModule autoeat = Hub.field_2595.method_78(AutoEatModule.class);
   private static UnfocusedFPSModule unfocusedfps = Hub.field_2595.method_78(UnfocusedFPSModule.class);
   private static AntiQuitModule antiquit = Hub.field_2595.method_78(AntiQuitModule.class);
   private static AbstractModule_38 nohitdelay = Hub.field_2595.method_78(AbstractModule_38.class);
   private static AutoClickerModule autoclicker = Hub.field_2595.method_78(AutoClickerModule.class);
   private static final ArrowsModule arrows = Hub.field_2595.method_78(ArrowsModule.class);
   private static final AutoCrystalModule autocrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   private static final FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   private static final FreeLookModule freelook = Hub.field_2595.method_78(FreeLookModule.class);
   @Unique
   private Screen mio$lastScreen;
   @Unique
   private boolean mio$freecamPickSet = false;
   @Unique
   private double mio$savedX;
   @Unique
   private double mio$savedY;
   @Unique
   private double mio$savedZ;
   @Unique
   private float mio$savedYRot;
   @Unique
   private float mio$savedXRot;
   @Shadow
   public int missTime;
   @Shadow
   @Final
   public Options options;
   @Shadow
   @Nullable
   public Screen screen;
   @Shadow
   @Final
   private Window window;
   private static boolean saved = false;

   public MixinMinecraft() {
      super();
   }

   @Inject(
      method = {"startAttack()Z"},
      at = {@At("HEAD")}
   )
   public void doAttackHook(CallbackInfoReturnable<Boolean> var1) {
      if (nohitdelay.isToggled() || autoclicker.isToggled()) {
         this.missTime = 0;
      }
   }

   @Inject(
      method = {"destroy"},
      at = {@At("HEAD")}
   )
   public void scheduleStop(CallbackInfo var1) {
      if (!saved && Hub.field_2597 != null) {
         saved = true;
         clickgui.disable();
         Hub.field_2597.method_357();
      }
   }

   @Inject(
      method = {"disconnect(Lnet/minecraft/client/gui/screens/Screen;Z)V"},
      at = {@At("HEAD")}
   )
   public void disconnect(Screen var1, boolean var2, CallbackInfo var3) {
      DisconnectEvent var4 = new DisconnectEvent();
      MinecraftAccess.lookups.method_36(var4);
   }

   @ModifyExpressionValue(
      method = {"continueAttack"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
      )}
   )
   public boolean handleBlockBreakingHook(boolean var1) {
      return !multitask.isToggled() && var1;
   }

   @Redirect(
      method = {"startUseItem"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;isDestroying()Z"
      ),
      require = 0
   )
   public boolean doItemUseHook(MultiPlayerGameMode var1) {
      return !multitask.isToggled() && var1.isDestroying();
   }

   @Inject(
      method = {"setScreen"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void setScreenHookPre(Screen var1, CallbackInfo var2) {
      this.mio$lastScreen = this.screen;
      ScreenOpenEvent var3 = new ScreenOpenEvent(this.mio$lastScreen, var1);
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"setScreen"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/screens/Screen;init(II)V",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void setScreenHookPost(Screen var1, CallbackInfo var2) {
      ScreenOpenPostEvent var3 = new ScreenOpenPostEvent(this.mio$lastScreen, var1);
      MinecraftAccess.lookups.method_36(var3);
      if (var3.method_464()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")}
   )
   private void tickHook(CallbackInfo var1) {
      MinecraftAccess.lookups.method_36(Event_21.field_3173);
   }

   @Inject(
      method = {"pick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void mio$pickHook(float var1, CallbackInfo var2) {
      Minecraft var3 = (Minecraft)(Object)this;
      if (freecam.isToggled() && var3.player != null && !this.mio$freecamPickSet) {
         Entity var4 = var3.player;
         this.mio$savedX = var4.getX();
         this.mio$savedY = var4.getY();
         this.mio$savedZ = var4.getZ();
         this.mio$savedYRot = var4.getYRot();
         this.mio$savedXRot = var4.getXRot();
         var4.setPos(freecam.field_2828.x, freecam.field_2828.y - (double)var4.getEyeHeight(var4.getPose()), freecam.field_2828.z);
         var4.setYRot(freecam.field_1760);
         var4.setYHeadRot(freecam.field_1760);
         var4.setXRot(freecam.field_1761);
         this.mio$freecamPickSet = true;
      }

      if (freelook.isToggled() && !freecam.isToggled()) {
         var2.cancel();
      }
   }

   @Inject(
      method = {"pick"},
      at = {@At("RETURN")}
   )
   private void mio$pickPostHook(float var1, CallbackInfo var2) {
      if (this.mio$freecamPickSet) {
         Minecraft var3 = (Minecraft)(Object)this;
         if (var3.player != null) {
            var3.player.setPos(this.mio$savedX, this.mio$savedY, this.mio$savedZ);
            var3.player.setYRot(this.mio$savedYRot);
            var3.player.setYHeadRot(this.mio$savedYRot);
            var3.player.setXRot(this.mio$savedXRot);
         }

         this.mio$freecamPickSet = false;
      }
   }

   @ModifyExpressionValue(
      method = {"handleKeybinds"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
      )}
   )
   private boolean handleInputEventsHook(boolean var1) {
      return autoeat.method_892() && autoeat.isToggled() ? false : var1;
   }

   @WrapWithCondition(
      method = {"runTick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Minecraft;stop()V"
      )}
   )
   private boolean renderHook(Minecraft var1) {
      if (antiquit.isToggled() && antiquit.gameClose.getValue()) {
         if (!(var1.screen instanceof CloseConfirmScreen)) {
            var1.setScreen(new CloseConfirmScreen());
         }

         return false;
      } else {
         return true;
      }
   }

   @Inject(
      method = {"runTick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Minecraft;runAllTasks()V",
         shift = Shift.AFTER
      )}
   )
   private void render(boolean var1, CallbackInfo var2, @Local int var3) {
      MinecraftAccess.lookups.method_36(new FrameRenderEvent(var3 >= 1));
   }

   @ModifyExpressionValue(
      method = {"handleKeybinds"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
      )}
   )
   private boolean handleInputEvents(boolean var1) {
      return arrows.isToggled() && arrows.autoShoot.getValue() ? false : var1;
   }

   @Inject(
      method = {"startUseItem"},
      at = {@At("HEAD")}
   )
   private void doItemUse(CallbackInfo var1) {
      autocrystal.method_463();
   }

   @ModifyExpressionValue(
      method = {"startUseItem"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"
      )}
   )
   private boolean doItemUseHook(boolean var1) {
      return multitask.isToggled() ? false : var1;
   }

   @ModifyExpressionValue(
      method = {"startAttack"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"
      )}
   )
   private boolean doAttack(boolean var1) {
      return multitask.isToggled() ? false : var1;
   }
}
