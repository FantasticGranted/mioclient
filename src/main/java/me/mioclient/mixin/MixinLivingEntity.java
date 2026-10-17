package me.mioclient.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.mioclient.Hub;
import me.mioclient.api.ServerSideDeadAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.CombatVersion;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.enum_.StrafeType;
import me.mioclient.event.StatusEffectEvent;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.Event_64;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.Class_0654;
import me.mioclient.internal.FakePlayerEntity;
import me.mioclient.internal.PacketSender;
import me.mioclient.internal.VectorUtil;
import me.mioclient.module.misc.SwingModule;
import me.mioclient.module.movement.ElytraFlyModule;
import me.mioclient.module.movement.FastLadderModule;
import me.mioclient.module.movement.FireworksModule;
import me.mioclient.module.movement.JesusModule;
import me.mioclient.module.movement.NoJumpDelayModule;
import me.mioclient.module.movement.SpeedModule;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.core.Holder;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({LivingEntity.class})
public abstract class MixinLivingEntity extends Entity implements ServerSideDeadAccess {
   @Shadow
   private int noJumpDelay;
   @Unique
   private Event_64 mio$moveEvent;
   @Unique
   private TravelEvent event;
   @Unique
   private boolean serverDead;
   @Unique
   private boolean prevFlying;
   @Unique
   private float prevLookYaw;
   @Unique
   private float prevLookPitch;
   @Unique
   private long prevTime = 0L;
   private static ElytraFlyModule efly = Hub.field_2595.method_78(ElytraFlyModule.class);
   private static NoJumpDelayModule njd = Hub.field_2595.method_78(NoJumpDelayModule.class);
   private static JesusModule jesus = Hub.field_2595.method_78(JesusModule.class);
   private static SwingModule swing = Hub.field_2595.method_78(SwingModule.class);
   private static final SpeedModule speed = Hub.field_2595.method_78(SpeedModule.class);
   private static final FireworksModule fireworks = Hub.field_2595.method_78(FireworksModule.class);
   private static final FastLadderModule fastladder = Hub.field_2595.method_78(FastLadderModule.class);

   public MixinLivingEntity(EntityType<?> var1, Level var2) {
      super(var1, var2);
   }

   @Inject(
      method = {"getCurrentSwingDuration"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getHandSwingDurationHook(CallbackInfoReturnable<Integer> var1) {
      if (swing.isToggled()) {
         LivingEntity var2 = (LivingEntity)(Object)this;
         var1.cancel();
         boolean var3 = swing.type.getValue() == CombatVersion.ONE_EIGHT;
         int var4 = (int)((double)(var3 ? 7 : 6) / swing.speed.getValue());
         if (MobEffectUtil.hasDigSpeed(var2)) {
            var1.setReturnValue(var4 - (1 + MobEffectUtil.getDigSpeedAmplification(var2)));
         } else {
            var1.setReturnValue(
               var2.hasEffect(MobEffects.MINING_FATIGUE) ? var4 + (1 + var2.getEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : var4
            );
         }
      }
   }

   @Inject(
      method = {"isFallFlying"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void isFallFlyingHook(CallbackInfoReturnable<Boolean> var1) {
      if (MinecraftAccess.method_31(this)) {
         if (fireworks.isToggled() && fireworks.fastLaunch.getValue() && fireworks.method_785() > 0) {
            var1.cancel();
            var1.setReturnValue(true);
         }

         if (efly.method_1178() || efly.method_1183()) {
            var1.setReturnValue(false);
            var1.cancel();
         }
      }
   }

   @Inject(
      method = {"canStandOnFluid"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void canWalkOnFluidHook(CallbackInfoReturnable<Boolean> var1) {
      if ((LivingEntity)(Object)this == Minecraft.getInstance().player && jesus.isToggled() && jesus.method_442() && !Minecraft.getInstance().player.isUnderWater()) {
         var1.setReturnValue(true);
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("HEAD")}
   )
   private void travelPreHook(Vec3 var1, CallbackInfo var2) {
      if ((LivingEntity)(Object)this == Minecraft.getInstance().player) {
         ElytraFlyModule.field_885 = true;
         this.prevLookYaw = this.getYRot(1.0F);
         this.prevLookPitch = this.getXRot();
         this.event = new TravelEvent(this.prevLookYaw, this.prevLookPitch, false);
         MinecraftAccess.lookups.method_36(this.event);
         if (this.event.method_464()) {
            this.setYRot(this.event.method_500());
            this.setXRot(this.event.method_501());
         }
      }
   }

   @Inject(
      method = {"travel"},
      at = {@At("TAIL")}
   )
   private void travelPostHook(Vec3 var1, CallbackInfo var2) {
      if ((LivingEntity)(Object)this == Minecraft.getInstance().player) {
         ElytraFlyModule.field_885 = false;
         if (this.event.method_464()) {
            this.setYRot(this.prevLookYaw);
            this.setXRot(this.prevLookPitch);
         }
      }
   }

   @ModifyExpressionValue(
      method = {"jumpFromGround"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;getJumpPower()F"
      )}
   )
   private float jumpFix(float var1) {
      this.event = new TravelEvent(this.getYRot(), this.getXRot(), true);
      MinecraftAccess.lookups.method_36(this.event);
      return this.event.method_464() ? this.event.method_500() : var1;
   }

   @Override
   public void setServerSideDead(boolean var1) {
      this.serverDead = var1;
   }

   @Override
   public boolean isServerSideDead() {
      return this.serverDead;
   }

   @Inject(
      method = {"aiStep"},
      at = {@At("HEAD")}
   )
   public void reduceCooldown(CallbackInfo var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (efly.isToggled() && efly.mode.getValue() == ElytraFlyMode.BOUNCE && this.equals(var2.player) && this.noJumpDelay > 2) {
         this.noJumpDelay = 2;
      } else if (njd.isToggled() && this.equals(var2.player) && !var2.player.isFallFlying()) {
         this.noJumpDelay = 0;
      }
   }

   @Inject(
      method = {"isFallFlying"},
      at = {@At("TAIL")},
      cancellable = true
   )
   public void onRecast(CallbackInfoReturnable<Boolean> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if ((LivingEntity)(Object)this == var2.player) {
         if (efly.method_1183()) {
            var1.setReturnValue(false);
         } else {
            long var3 = System.currentTimeMillis();
            long var5 = var3 - this.prevTime;
            if (var5 >= 12L) {
               if (efly.isToggled() && efly.mode.getValue() == ElytraFlyMode.BOUNCE) {
                  boolean var7 = (Boolean)var1.getReturnValue();
                  if (this.prevFlying && !var7) {
                     if (efly.method_1179()) {
                        PacketSender.method_1099();
                     }

                     var1.setReturnValue(efly.method_1179());
                  }

                  this.prevFlying = var7;
               }

               this.prevTime = var3;
            }
         }
      }
   }

   @Inject(
      method = {"hasEffect"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void hasStatusEffectHook(Holder<MobEffect> var1, CallbackInfoReturnable<Boolean> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.equals(var3.player)) {
         StatusEffectEvent var4 = new StatusEffectEvent(var1);
         MinecraftAccess.lookups.method_36(var4);
         if (var4.method_464()) {
            var2.cancel();
            var2.setReturnValue(false);
         }
      }
   }

   @Inject(
      method = {"getEffect"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void getStatusEffectHook(Holder<MobEffect> var1, CallbackInfoReturnable<MobEffectInstance> var2) {
      Minecraft var3 = Minecraft.getInstance();
      if (this.equals(var3.player)) {
         StatusEffectEvent var4 = new StatusEffectEvent(var1);
         MinecraftAccess.lookups.method_36(var4);
         if (var4.method_464()) {
            var2.cancel();
            var2.setReturnValue(null);
         }
      }
   }

   @Inject(
      method = {"jumpFromGround"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void jumpHook(CallbackInfo var1) {
      if (!speed.method_404()) {
         if (speed.isToggled() && speed.mode.getValue() == StrafeType.STRAFE_STRICT && MovementUtil.method_363()) {
            var1.cancel();
         }
      }
   }

   @ModifyConstant(
      method = {"handleRelativeFrictionAndCalculateMovement"},
      constant = {@Constant(
         doubleValue = 0.2
      )}
   )
   private double applyMovementInputHook(double var1) {
      return fastladder.isToggled() ? (double)fastladder.speed.getValue().floatValue() : var1;
   }

   @Inject(
      method = {"handleRelativeFrictionAndCalculateMovement"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V",
         shift = Shift.BEFORE
      )}
   )
   private void tickPre(Vec3 var1, float var2, CallbackInfoReturnable<Vec3> var3) {
      if (Event_64.method_33(this)) {
         this.mio$moveEvent = new Event_64(this.getDeltaMovement());
         MinecraftAccess.lookups.method_36(this.mio$moveEvent);
         VectorUtil.method_2(this.getDeltaMovement(), this.mio$moveEvent.method_380(), this.mio$moveEvent.method_395(), this.mio$moveEvent.method_396());
      }
   }
}
