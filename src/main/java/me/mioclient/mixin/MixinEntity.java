package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReceiver;
import me.mioclient.Hub;
import me.mioclient.api.CameraAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.api.SuperDamageAccess;
import me.mioclient.enum_.PreType;
import me.mioclient.event.EntityMoveEvent;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.FakePlayerEntity;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.movement.NoSlowModule;
import me.mioclient.module.movement.VelocityModule;
import me.mioclient.module.player.FreecamModule;
import me.mioclient.module.render.FreeLookModule;
import me.mioclient.module.render.NoRenderModule;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
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

@Mixin({Entity.class})
public abstract class MixinEntity implements CameraAccess {
   private static NoSlowModule noslow = Hub.field_2595.method_78(NoSlowModule.class);
   private static ElytraFlyModule elytrafly = Hub.field_2595.method_78(ElytraFlyModule.class);
   private static FreecamModule freecam = Hub.field_2595.method_78(FreecamModule.class);
   @Shadow
   private float yRot;
   @Shadow
   private float xRot;
   private static VelocityModule velo = Hub.field_2595.method_78(VelocityModule.class);
   private static FreeLookModule fl = Hub.field_2595.method_78(FreeLookModule.class);
   private static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   @Unique
   private float cameraPitch;
   @Unique
   private float cameraYaw;
   @Unique
   private Vec3 mio$prevVelocity;

   public MixinEntity() {
      super();
   }

   @Shadow
   private Vec3 collide(Vec3 var1) {
      return null;
   }

   @Shadow
   public abstract void move(MoverType var1, Vec3 var2);

   @Shadow
   @Override
   public abstract boolean equals(Object var1);

   @Shadow
   public abstract void setPos(Vec3 var1);

   @Shadow
   public abstract void setPos(double var1, double var3, double var5);

   @Shadow
   protected abstract void setRot(float var1, float var2);

   @Shadow
   public abstract Vec3 getDeltaMovement();

   @Shadow
   public abstract boolean hasPose(Pose var1);

   @ModifyReceiver(
      method = {"getBlockSpeedFactor"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/level/block/Block;getSpeedFactor()F"
      )},
      require = 0
   )
   private Block getVelocityMultiplierHook(Block var1) {
      if ((Entity)(Object)this != Minecraft.getInstance().player) {
         return var1;
      } else {
         return noslow.isToggled() && noslow.soulSand.getValue() ? Blocks.STONE : var1;
      }
   }

   @Inject(
      method = {"push(Lnet/minecraft/world/entity/Entity;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void pushAwayFromHook(Entity var1, CallbackInfo var2) {
      if (this.equals(Minecraft.getInstance().player) && velo.isToggled() && velo.push.getValue()) {
         var2.cancel();
      }
   }

   @Redirect(
      method = {"updateFluidInteraction"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/EntityFluidInteraction;applyCurrentTo(Lnet/minecraft/tags/TagKey;Lnet/minecraft/world/entity/Entity;D)V"
      )
   )
   private void updateMovementInFluidHook(
      net.minecraft.world.entity.EntityFluidInteraction var1, net.minecraft.tags.TagKey var2, Entity var3, double var4
   ) {
      if (!(velo.isToggled() && velo.liquids.getValue())) {
         var1.applyCurrentTo(var2, var3, var4);
      }
   }

   @Redirect(
      method = {"move"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;collide(Lnet/minecraft/world/phys/Vec3;)Lnet/minecraft/world/phys/Vec3;"
      )
   )
   private Vec3 moveHook(Entity var1, Vec3 var2) {
      if ((Entity)(Object)this != Minecraft.getInstance().player) {
         return this.collide(var2);
      } else {
         EntityMoveEvent var3 = new EntityMoveEvent(PreType.PRE, 0.6F);
         MinecraftAccess.lookups.method_36(var3);
         EntityUtil.method_9((LivingEntity)(Object)this, var3.method_575());
         Vec3 var4 = this.collide(var2);
         if (var4 != null) {
            MinecraftAccess.lookups.method_36(new EntityMoveEvent(PreType.POST, (float)var4.y));
         }

         return var4;
      }
   }

   @Inject(
      method = {"getPose"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getPoseHook(CallbackInfoReturnable<Pose> var1) {
      if ((Entity)(Object)this == Minecraft.getInstance().player) {
         if (elytrafly.method_1178() || elytrafly.method_1183()) {
            var1.setReturnValue(Pose.STANDING);
            var1.cancel();
         }
      }
   }

    @Inject(
       method = {"hurt"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private void fakePlayerHurtHook(DamageSource var1, float var2, CallbackInfo var3) {
       if ((Object)this instanceof FakePlayerEntity) {
          ((SuperDamageAccess)this).damageSuper(var1, var2);
          var3.cancel();
       }
    }

    @Inject(
       method = {"hurtOrSimulate"},
       at = {@At("HEAD")},
       cancellable = true
    )
    private void fakePlayerHurtOrSimulateHook(DamageSource var1, float var2, CallbackInfoReturnable<Boolean> var3) {
       if ((Object)this instanceof FakePlayerEntity) {
          var3.setReturnValue(((SuperDamageAccess)this).damageSuper(var1, var2));
       }
    }

    @Inject(
       method = {"turn"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void changeCameraLookDirection(double var1, double var3, CallbackInfo var5) {
      if (!freecam.isToggled()) {
         if (fl.isToggled() && this.equals(Minecraft.getInstance().player)) {
            double var6 = var3 * 0.15;
            double var8 = var1 * 0.15;
            this.cameraPitch = Mth.clamp(this.cameraPitch + (float)var6, (float)(-Class_0245.field_685), (float)Class_0245.field_685);
            this.cameraYaw += (float)var8;
            var5.cancel();
         }
      }
   }

   @Inject(
      method = {"lerpPositionAndRotationStep"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void lerpPosAndRotationHook(int var1, double var2, double var4, double var6, double var8, double var10, CallbackInfo var12) {
      if (norender.isToggled() && norender.interpolation.getValue()) {
         this.setPos(var2, var4, var6);
         this.setRot((float)var8, (float)var10);
         var12.cancel();
      }
   }

   @ModifyExpressionValue(
      method = {"getEyePosition(F)Lnet/minecraft/world/phys/Vec3;"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/Entity;getEyeHeight()F"
      )}
   )
   private float getHeightHook(float var1) {
      return norender.isToggled() && norender.newSneaking.getValue() && this.hasPose(Pose.CROUCHING) ? 1.54F : var1;
   }

   @Inject(
      method = {"baseTick"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/util/profiling/ProfilerFiller;push(Ljava/lang/String;)V",
         ordinal = 0,
         shift = Shift.AFTER
      )}
   )
   private void baseTick(CallbackInfo var1) {
      this.mio$prevVelocity = this.getDeltaMovement();
   }

   @Inject(
      method = {"<init>"},
      at = {@At("TAIL")}
   )
   private void init(EntityType var1, Level var2, CallbackInfo var3) {
      this.mio$prevVelocity = Vec3.ZERO;
   }

   @Unique
   @Override
   public float getCameraPitch() {
      return this.cameraPitch;
   }

   @Unique
   @Override
   public float getCameraYaw() {
      return this.cameraYaw;
   }

   @Unique
   @Override
   public void setCameraPitch(float var1) {
      this.cameraPitch = var1;
   }

   @Unique
   @Override
   public void setCameraYaw(float var1) {
      this.cameraYaw = var1;
   }

   @Override
   public Vec3 mio$getPrevVelocity() {
      return this.mio$prevVelocity;
   }
}
