package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.SprintMode;
import me.mioclient.enum_.Class_1054;
import me.mioclient.enum_.ElytraFlyMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.AttackEntityEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.SprintEvent;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.internal.PacketSender;
import me.mioclient.mixin.ducks.DuckEntity;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.player.RotationLockModule;
import me.mioclient.module.player.ScaffoldModule;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class SprintModule extends Module {
   public static final ScaffoldModule field_1016 = Hub.field_2595.method_78(ScaffoldModule.class);
   public static final ElytraFlyModule efly = Hub.field_2595.method_78(ElytraFlyModule.class);
   public static final LongJumpModule field_1017 = Hub.field_2595.method_78(LongJumpModule.class);
   public static final FastFallModule field_1018 = Hub.field_2595.method_78(FastFallModule.class);
   public static final SpeedModule field_1019 = Hub.field_2595.method_78(SpeedModule.class);
   public static final FastWebModule field_1020 = Hub.field_2595.method_78(FastWebModule.class);
   public static final AbstractModule_28 field_1021 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static RotationLockModule field_711 = Hub.field_2595.method_78(RotationLockModule.class);
   public Setting<SprintMode> mode;
   public Setting<Boolean> forceSprint;
   public Setting<Boolean> vanillaSlowdown;
   public Setting<Boolean> boost;
   public Setting<Float> boostSpeed;
   public Setting<Boolean> stopInLiquids;
   public Setting<Boolean> directionSpoof;
   public Setting<Boolean> keep;
   public final Timer field_1030;
   public float field_1031;
   public boolean field_1032;
   public boolean field_1033;

   public SprintModule() {
      super("Sprint", "Sprints automatically.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_1030 = new Timer();
      this.field_1031 = 0.0F;
   }

   @Override
   public String getInfo() {
      return FontRenderer.method_3(this.mode.getValue());
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_1031 = 0.0F;
      }
   }

   @Subscribe
   public void method_5(InteractBlockEvent var1) {
      if (!Hub.field_2630.method_261()) {
         if (field_1021.movementSync.getValue() && field_1021.rotations.getValue() == Class_1054.SILENT) {
            if (this.directionSpoof.getValue()
               && Hub.field_2598.method_510() == null
               && MovementUtil.method_363()
               && MovementUtil.method_496() != mc.player.getYRot()
               && !this.field_1033) {
               PacketSender.method_2(
                  new PosRot(
                     mc.player.getX(),
                     mc.player.getY(),
                     mc.player.getZ(),
                     mc.player.getYRot(),
                     mc.player.getXRot(),
                     mc.player.onGround()
                  , false)
               );
            }
         }
      }
   }

   @Subscribe
   public void method_2(ExplosionEvent var1) {
      if (!Hub.field_2630.method_261()) {
         Vec3 var2 = new Vec3(var1.method_380(), var1.method_395(), var1.method_396());
         if (MovementUtil.method_363()
            && Hub.field_2602.method_984().method_9(750L)
            && this.boost.getValue()
            && !(Math.sqrt(mc.player.distanceToSqr(var2)) > 6.0)) {
            this.field_1030.reset();
            this.field_1031 = (float)Math.hypot((double)var1.method_398(), (double)var1.method_400()) * this.boostSpeed.getValue();
         }
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_9(Event_19 var1) {
      if (!Hub.field_2630.method_261()) {
         if (!this.method_535() && !this.method_364() && !field_711.isToggled()) {
            if (this.directionSpoof.getValue() && MovementUtil.method_363() && !mc.player.isFallFlying()) {
               var1.setYaw(MovementUtil.method_496());
            }
         }
      }
   }

   @Subscribe
   public void method_9(SprintEvent var1) {
      if (!Hub.field_2630.method_261()) {
         if (!this.method_535() && !this.method_364()) {
            if (this.mode.getValue() != SprintMode.INSTANT || this.forceSprint.getValue()) {
               this.method_101(this.method_363());
            }
         }
      }
   }

   @Subscribe(
      method_800 = 1000
   )
   public void method_2(MoveEvent var1) {
      if (!Hub.field_2630.method_261()) {
         if (!this.method_535() && !this.method_364() && this.mode.getValue() == SprintMode.INSTANT) {
            if (!PlayerUtil.method_427()
               && !mc.player.isFallFlying()
               && !mc.player.getAbilities().flying
               && !mc.player.isInWater()
               && !mc.player.isInLava()
               && !mc.player.isShiftKeyDown()
               && !mc.player.isVisuallySwimming()
               && !field_1018.field_4406
               && !field_1017.isToggled()) {
               if (!field_1019.isToggled()) {
                  double var2 = MovementUtil.method_78(this.vanillaSlowdown.getValue());
                  if (Hub.field_2602.method_984().method_9(500L)) {
                     var2 = Math.max(var2, Math.hypot(var1.method_380(), var1.method_396()));
                  }

                  if (this.vanillaSlowdown.getValue()) {
                     Block var4 = mc.level.getBlockState(((DuckEntity)(Object)mc.player).mio$getVelocityAffectingPos()).getBlock();
                     if (var4 == Blocks.SLIME_BLOCK) {
                        var2 *= 0.7;
                     }

                     var2 *= (double)var4.getSpeedFactor();
                  }

                  if (this.boost.getValue() && !this.field_1030.method_9(500L)) {
                     var2 += (double)this.field_1031;
                  }

                  MovementUtil.method_2(var1, var2);
               }
            }
         }
      }
   }

   @Subscribe
   public void method_9(TravelEvent var1) {
      if (!Hub.field_2630.method_261()) {
         if (this.directionSpoof.getValue()
            && MovementUtil.method_363()
            && !mc.player.isFallFlying()
            && (var1.method_1025() || mc.player.isVisuallySwimming())
            && !this.method_364()) {
            if (mc.player.isVisuallyCrawling() && !var1.method_1025() || field_711.isToggled()) {
               return;
            }

            var1.setYaw(MovementUtil.method_496());
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_9(AttackEntityEvent var1) {
      if (!Hub.field_2630.method_261()) {
         if (this.keep.getValue()) {
            if (var1.method_213() == PreType.PRE) {
               this.field_1032 = mc.player.isSprinting();
               if (this.field_1032) {
                  PacketSender.method_2(mc.player, Action.STOP_SPRINTING, 0);
               }
            } else if (this.field_1032) {
               mc.player.setSprinting(true);
               PacketSender.method_2(mc.player, Action.START_SPRINTING, 0);
            }
         }
      }
   }

   public boolean method_363() {
      if (this.method_364()) {
         return false;
      } else if (!this.directionSpoof.getValue() || Hub.field_2598.method_510() == null && !mc.player.isFallFlying()) {
         boolean var1 = this.mode.getValue() == SprintMode.LEGIT || mc.player.isUnderWater();
         if (PlayerUtil.method_427() || var1 && (mc.player.input.moveVector.y <= 0.0F || mc.player.horizontalCollision) || this.method_365()
            )
          {
            return false;
         } else {
            return !field_1020.method_726() || !PlayerUtil.method_5(mc.player) || Hub.field_2615.method_1161() >= 2 && !mc.player.input.keyPresses.jump()
               ? mc.player.input.moveVector.y != 0.0F || mc.player.input.moveVector.x != 0.0F
               : false;
         }
      } else {
         return false;
      }
   }

   public boolean method_364() {
      return Hub.field_2630.method_264() && Hub.field_2630.method_261()
         ? true
         : field_1016.isToggled() && field_1016.stopSprinting.getValue()
            || PlayerUtil.method_29(mc.player) && this.stopInLiquids.getValue()
            || efly.isToggled() && efly.mode.getValue() == ElytraFlyMode.BOUNCE && efly.method_558();
   }

   public void method_101(boolean var1) {
      try {
         mc.player.setSprinting(var1);
      } catch (Exception var3) {
      }
   }

   public boolean method_365() {
      return mc.player.onGround()
         ? false
         : BlockPos.betweenClosedStream(mc.player.getBoundingBox().expandTowards(0.0, -0.1, 0.0))
            .anyMatch(var0 -> mc.level.isWaterAt(var0));
   }
}
