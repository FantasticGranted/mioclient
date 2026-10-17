package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import me.mioclient.Hub;
import me.mioclient.api.PlayerTickHook;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.SwingHand;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.SprintEvent;
import me.mioclient.event.TickMovementEvent;
import me.mioclient.event.PushOutOfBlocksEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.module.misc.SwingModule;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.movement.EntityControlModule;
import me.mioclient.module.movement.NoSlowModule;
import me.mioclient.module.movement.SprintModule;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.ClientInput;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LocalPlayer.class})
public abstract class MixinClientPlayerEntity extends AbstractClientPlayer implements MinecraftAccess, PlayerTickHook {
   private static ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);
   private static SwingModule swing = Hub.field_2595.method_78(SwingModule.class);
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);
   private static EntityControlModule entitycontrol = Hub.field_2595.method_78(EntityControlModule.class);
   private static SprintModule sprint = Hub.field_2595.method_78(SprintModule.class);
   @Shadow
   public ClientInput input;
   @Unique
   private Event_19 motionEvent = new Event_19();

   public MixinClientPlayerEntity(ClientLevel var1, GameProfile var2) {
      super(var1, var2);
   }

   @Shadow
   protected abstract void updateAutoJump(float var1, float var2);

   @Shadow
   public abstract float getViewXRot(float tickDelta);

   @Shadow
   public abstract float getViewYRot(float tickDelta);

   @Shadow
   public abstract void resetPos();

   @Shadow
   public abstract boolean isUsingItem();

   @Shadow
   private void sendPosition() {
   }

   @Inject(
      method = {"swing"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void swingHandHook(InteractionHand var1, CallbackInfo var2) {
      if (swing.isToggled() && swing.hand.getValue() != SwingHand.VANILLA) {
         if (swing.hand.getValue() != SwingHand.PACKET) {
            super.swing(swing.hand.getValue().method_12());
         }

         mc.player.connection.send(new ServerboundSwingPacket(var1));
         var2.cancel();
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
         shift = Shift.BEFORE
      )}
   )
   public void tickPost(CallbackInfo var1) {
      if (mc.player != null && mc.level != null) {
         lookups.method_36(new PostTickEvent());
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At("RETURN")}
   )
   public void tick(CallbackInfo var1) {
      if (mc.player != null && mc.level != null) {
         lookups.method_36(new PlayerTickEvent());
      }
   }

   @Inject(
      method = {"move"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void move(MoverType var1, Vec3 var2, CallbackInfo var3) {
      Vec3 var4 = new Vec3(var2.x, var2.y, var2.z);
      MoveEvent var5 = new MoveEvent(var2, var1);
      lookups.method_36(var5);
      if (!var5.method_464()) {
         if (var4.equals(var5.method_1066())) {
            return;
         }

         var3.cancel();
         double var6 = this.getX();
         double var8 = this.getZ();
         super.move(var5.method_1067(), var5.method_1066());
         this.updateAutoJump((float)(this.getX() - var6), (float)(this.getZ() - var8));
      } else {
         var3.cancel();
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/AbstractClientPlayer;tick()V",
         shift = Shift.AFTER
      )},
      cancellable = true
   )
   private void tickPreHook(CallbackInfo var1) {
      this.resetEvent();
      lookups.method_36(this.motionEvent);
      this.setPos(this.motionEvent.method_380(), this.motionEvent.method_395(), this.motionEvent.method_396());
      this.setYRot(this.motionEvent.method_704());
      this.setXRot(this.motionEvent.method_705());
      this.setOnGround(this.motionEvent.method_707());
      if (this.motionEvent.method_464()) {
         var1.cancel();
      }
   }

   @Inject(
      method = {"tick"},
      at = {@At(
         value = "INVOKE",
         target = "Ljava/util/List;iterator()Ljava/util/Iterator;",
         shift = Shift.BEFORE
      )},
      cancellable = true
   )
   private void tickPostHook(CallbackInfo var1) {
      if (this.motionEvent.method_380() != this.motionEvent.method_698()
         || this.motionEvent.method_395() != this.motionEvent.method_699()
         || this.motionEvent.method_396() != this.motionEvent.method_700()) {
         this.setPos(new Vec3(this.motionEvent.method_698(), this.motionEvent.method_699(), this.motionEvent.method_700()));
      }

      this.resetRotations();
      if (this.onGround() == this.motionEvent.method_707()) {
         this.setOnGround(this.motionEvent.method_703());
      }
   }

   @Inject(
      method = {"sendPosition"},
      at = {@At("RETURN")}
   )
   private void sendMovementPacketsPostHook(CallbackInfo var1) {
      Event_19 var2 = new Event_19(PreType.POST, this.motionEvent);
      if (this.motionEvent.method_464()) {
         var2.method_463();
      }

      lookups.method_36(var2);
   }

   @ModifyExpressionValue(
      method = {"sendPosition"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;getX()D"
      )}
   )
   private double getXHook(double var1) {
      return this.motionEvent.method_380();
   }

   @ModifyExpressionValue(
      method = {"sendPosition"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;getY()D"
      )}
   )
   private double getYHook(double var1) {
      return this.motionEvent.method_395();
   }

   @ModifyExpressionValue(
      method = {"sendPosition"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;getZ()D"
      )}
   )
   private double getZHook(double var1) {
      return this.motionEvent.method_396();
   }

   @ModifyExpressionValue(
      method = {"sendPosition"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;getYRot()F"
      )}
   )
   private float getYawHook(float var1) {
      if (this.motionEvent.method_701() != var1) {
         return var1;
      } else {
         return this.motionEvent.method_701() == this.motionEvent.method_500() ? var1 : this.motionEvent.method_500();
      }
   }

   @ModifyExpressionValue(
      method = {"sendPosition"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;getXRot()F"
      )}
   )
   private float getPitchHook(float var1) {
      if (this.motionEvent.method_702() != var1) {
         return var1;
      } else {
         return this.motionEvent.method_702() == this.motionEvent.method_501() ? var1 : this.motionEvent.method_501();
      }
   }

   @ModifyExpressionValue(
      method = {"sendPosition"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;onGround()Z"
      )}
   )
   private boolean isOnGroundHook(boolean var1) {
      return this.motionEvent.method_707();
   }

   @Redirect(
      method = {"aiStep"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;setSprinting(Z)V",
         ordinal = 3
      ),
      require = 0
   )
   private void setSprintingHook(LocalPlayer var1, boolean var2) {
      var1.setSprinting(sprint.isToggled() && sprint.method_363());
   }

   @Inject(
      method = {"aiStep"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;canStartSprinting()Z"
      )}
   )
   private void onSprintUpdate(CallbackInfo var1) {
      lookups.method_36(new SprintEvent());
   }

    @ModifyExpressionValue(
       method = {"aiStep"},
       at = {@At(
          value = "INVOKE",
          target = "Lnet/minecraft/client/player/LocalPlayer;isSlowDueToUsingItem()Z"
       )}
    )
    private boolean tickMovementHook(boolean var1) {
      TickMovementEvent var2 = new TickMovementEvent();
      lookups.method_36(var2);
      return var2.method_464() ? false : var1;
   }

   @Inject(
      method = {"moveTowardsClosestSpace"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void pushOutOfBlocksHook(double var1, double var3, CallbackInfo var5) {
      PushOutOfBlocksEvent var6 = new PushOutOfBlocksEvent();
      lookups.method_36(var6);
      if (var6.method_464()) {
         var5.cancel();
      }
   }

   @Inject(
      method = {"getJumpRidingScale"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getJumpMountStrengthHook(CallbackInfoReturnable<Float> var1) {
      if (entitycontrol.isToggled() && entitycontrol.horseJump.getValue()) {
         var1.setReturnValue(1.0F);
      }
   }

   @Inject(
      method = {"handlePortalTransitionEffect"},
      at = {@At("HEAD")}
   )
   private void updateNauseaHook(CallbackInfo var1) {
      if (noslow.isToggled() && noslow.portals.getValue() && this.portalProcess != null) {
         this.portalProcess.setAsInsidePortalThisTick(false);
      }
   }

   @Inject(
      method = {"aiStep"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/player/LocalPlayer;isFallFlying()Z",
         shift = Shift.BEFORE
      )}
   )
   private void tickMovementElytra(CallbackInfo var1) {
      if (elytrafly.isToggled() && elytrafly.method_558()) {
         boolean var2 = mc.player.input.keyPresses.jump();
         if ((elytrafly.mode.getValue() != ElytraFlyMode.PACKET || elytrafly.vertical2.getValue() && mc.player.input.keyPresses.jump()) && var2) {
            elytrafly.method_1099();
         } else {
            Hub.field_2596.method_38(elytrafly);
         }
      }
   }

   @Inject(
      method = {"isMovingSlowly"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void shouldSlowdown(CallbackInfoReturnable<Boolean> var1) {
      boolean var2 = mc.player.isVisuallyCrawling() && noslow.crawl.getValue();
      boolean var3 = !var2 && mc.player.isShiftKeyDown() && noslow.sneak.getValue();
      if (noslow.isToggled() && (var2 || var3)) {
         var1.setReturnValue(false);
      }
   }

   @Override
   public void sendMovementPacketsWrapper() {
      this.sendPosition();
   }

   @Override
   public void superTick() {
      super.tick();
   }

   @Override
   public void resetEvent() {
      this.motionEvent = new Event_19(PreType.PRE, this.getX(), this.getBoundingBox().minY, this.getZ(), this.getYRot(), this.getXRot(), this.onGround());
   }

   @Override
   public void resetRotations() {
      Hub.field_2598
         .method_29(
            this.isPassenger() && !this.motionEvent.method_706() ? this.getVehicle().getYRot() : this.motionEvent.method_500(), this.motionEvent.method_501()
         );
      if (this.getYRot() == this.motionEvent.method_704()) {
         this.setYRot(this.motionEvent.method_701());
      }

      if (this.getXRot() == this.motionEvent.method_705()) {
         this.setXRot(this.motionEvent.method_702());
      }
   }
}
