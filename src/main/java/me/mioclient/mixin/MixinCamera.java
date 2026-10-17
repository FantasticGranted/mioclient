package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.mioclient.Hub;
import me.mioclient.api.CameraAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.event.CameraEvent;
import me.mioclient.internal.RenderUtil;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.module.render.FreeLookModule;
import me.mioclient.module.render.NoRenderModule;
import me.mioclient.module.render.ViewClipModule;
import me.mioclient.module.render.ZoomModule;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin({Camera.class})
public abstract class MixinCamera {
   private static ViewClipModule viewclip = Hub.field_2595.method_78(ViewClipModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   private static ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);
   private static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   private static FreeLookModule fl = Hub.field_2595.method_78(FreeLookModule.class);
   private static ZoomModule zoom = Hub.field_2595.method_78(ZoomModule.class);
   @Final
   @Shadow
   private Vector3f forwards;
   @Final
   @Shadow
   private Vector3f up;
   @Final
   @Shadow
   private Vector3f left;
   @Shadow
   private Vec3 position;
   @Shadow
   private float eyeHeight;
   @Shadow
   private float eyeHeightOld;
   @Shadow
   private Entity entity;
   @Unique
   private boolean hold = true;

   public MixinCamera() {
      super();
   }

   @Shadow
   protected void setPosition(Vec3 var1) {
   }

   @Shadow
   protected abstract void setRotation(float var1, float var2);

   @Inject(
      method = {"getMaxZoom"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void clipToSpaceHook(float var1, CallbackInfoReturnable<Float> var2) {
      if (viewclip.isToggled()) {
         var2.setReturnValue(viewclip.range.getValue().floatValue());
      }
   }

   @ModifyReturnValue(
      method = {"calculateFov"},
      at = {@At("RETURN")}
   )
   private float mio$zoomFov(float var1) {
      return zoom.isToggled() ? var1 * (float)(1.0 - (double)zoom.method_1176()) : var1;
   }

   @Inject(
      method = {"tick"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void updateEyeHeightHook(CallbackInfo var1) {
      if (norender.isToggled() && norender.newSneaking.getValue() && this.entity != null) {
         var1.cancel();
         this.eyeHeightOld = this.eyeHeight;
         if (this.entity instanceof Player var2) {
            if (var2.hasPose(Pose.CROUCHING)) {
               this.eyeHeight = 1.54F;
            } else if (this.eyeHeight < 1.62F && var2.hasPose(Pose.STANDING)) {
               float var4 = 1.62F - this.eyeHeight;
               var4 = (float)((double)var4 * 0.4);
               this.eyeHeight = 1.62F - var4;
            } else {
               this.eyeHeight = this.eyeHeight + (this.entity.getEyeHeight() - this.eyeHeight) * 0.5F;
            }
         }
      }
   }

   @Inject(
      method = {"move"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void moveByHook(float var1, float var2, float var3, CallbackInfo var4) {
      if (viewclip.isToggled() && viewclip.smooth.getValue()) {
         double var5 = (double)this.forwards.x() * (double)var1 + (double)this.up.x() * (double)var2 + (double)this.left.x() * (double)var3;
         double var7 = (double)this.forwards.y() * (double)var1 + (double)this.up.y() * (double)var2 + (double)this.left.y() * (double)var3;
         double var9 = (double)this.forwards.z() * (double)var1 + (double)this.up.z() * (double)var2 + (double)this.left.z() * (double)var3;
         float var11 = viewclip.field_4496.method_45();
         this.setPosition(
            new Vec3(this.position.x + var5 * (double)var11, this.position.y + var7 * (double)var11, this.position.z + var9 * (double)var11)
         );
         var4.cancel();
      }
   }

   @ModifyArgs(
      method = {"alignWithEntity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Camera;setPosition(DDD)V"
      )
   )
   private void onUpdateSetPosArgs(Args var1) {
      CameraEvent var2 = new CameraEvent((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
      MinecraftAccess.lookups.method_36(var2);
      if (var2.method_464()) {
         var1.set(0, var2.method_733().x);
         var1.set(1, var2.method_733().y);
         var1.set(2, var2.method_733().z);
      }
   }

   @ModifyArgs(
      method = {"alignWithEntity"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Camera;setRotation(FF)V"
      )
   )
   private void onUpdateSetRotationArgs(Args var1) {
      if (elytrafly.isToggled() && elytrafly.mode.getValue() == ElytraFlyMode.BOUNCE && elytrafly.pitchLock.getValue() && !elytrafly.silent.getValue()) {
         var1.set(1, elytrafly.pitch2.getValue());
      }

      float var2 = RenderUtil.method_776();
      if (freecam.isToggled()) {
         var1.set(0, (float)freecam.method_8(var2));
         var1.set(1, (float)freecam.method_334(var2));
      }
   }

   @Inject(
      method = {"alignWithEntity"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/Camera;setRotation(FF)V",
         ordinal = 1,
         shift = Shift.AFTER
      )}
   )
   public void lockRotation(float var1, CallbackInfo var2) {
      if (!freecam.isToggled()) {
         Entity var3 = this.entity;
         if (fl.isToggled() && var3 instanceof LocalPlayer) {
            Minecraft var4 = Minecraft.getInstance();
            CameraAccess var5 = (CameraAccess)var3;
            if (var4.player != null && this.hold) {
               var5.setCameraYaw(var4.player.getYRot());
               var5.setCameraPitch(var4.player.getXRot());
               this.hold = false;
            }

            this.setRotation(var5.getCameraYaw(), var5.getCameraPitch());
         }

         if (!fl.isToggled() && var3 instanceof LocalPlayer) {
            this.hold = true;
         }
      }
   }
}
