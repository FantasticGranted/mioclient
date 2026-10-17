package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.enum_.StrafeType;
import me.mioclient.event.Event_19;
import me.mioclient.event.ExplosionEvent;
import me.mioclient.event.SprintEvent;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.EntityMoveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.SpeedMode;
import me.mioclient.internal.Class_0963;
import me.mioclient.internal.Class_0992;
import me.mioclient.internal.Class_1177;
import me.mioclient.internal.Class_1197;
import me.mioclient.internal.EnumMapping;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.internal.Class_1376;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_6;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class SpeedModule extends Module {
   public static AbstractModule_6 field_1637 = Hub.field_2595.method_78(AbstractModule_6.class);
   public Setting<StrafeType> mode;
   public Setting<Float> speed;
   public Setting<Boolean> boost;
   public Setting<Boolean> inLiquid;
   public Setting<Boolean> useTimer;
   public Setting<Boolean> pauseInBlocks;
   public boolean field_2197;
   public final Timer field_2198;
   public final EnumMapping<StrafeType, SpeedMode> field_2199;

   public SpeedModule() {
      super("Speed", "Makes you move faster.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_2198 = new Timer();
      this.field_2199 = new EnumMapping<>(this.mode);
      this.field_2199.method_2(StrafeType.STRAFE, new Class_0992(this));
      this.field_2199.method_2(StrafeType.STRAFE_STRICT, new Class_1376(this));
      this.field_2199.method_2(StrafeType.VANILLA, new Class_1177(this));
      this.field_2199.method_2(StrafeType.ON_GROUND, new Class_0963(this));
      this.field_2199.method_2(StrafeType.GRIM, new Class_1197(this));
      this.field_2199.method_2(StrafeType.NONE, new SpeedMode(this) {
         @Override
         public void method_9(MoveEvent var1) {
         }
      });
   }

   @Override
   public String getInfo() {
      return this.mode.getValue().getName();
   }

   @Override
   public void onEnable() {
      this.field_2199.method_1156().onEnable();
   }

   @Subscribe
   public void method_2(SprintEvent var1) {
      if (this.mode.getValue() == StrafeType.STRAFE_STRICT && !this.method_404()) {
         mc.player.setSprinting(true);
      }
   }

   @Subscribe
   public void method_9(TravelEvent var1) {
      if (!var1.method_1025()) {
         this.field_2199.method_1156().method_738();
      }
   }

   @Subscribe
   public void method_9(MoveEvent var1) {
      if (Hub.field_2602.method_985().method_9(10L) && !this.field_2197) {
         this.field_2199.method_1156().method_9(var1);
      }
   }

   @Subscribe
   public void method_2(EntityMoveEvent var1) {
      if (var1.method_213() == PreType.POST && mc.player.onGround() && (double)var1.method_575() > 0.6) {
         this.field_2197 = true;
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_2(Event_19 var1) {
      this.field_2197 = false;
      if (Hub.field_2602.method_985().method_9(10L)) {
         this.field_2199.method_1156().method_2(var1);
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.reset();
      }
   }

   @Subscribe
   public void method_2(ExplosionEvent var1) {
      Vec3 var2 = new Vec3(var1.method_380(), var1.method_395(), var1.method_396());
      double var3 = (double)ExplosionDamage.method_2(
         var2, mc.player, mc.player.getBoundingBox(), 6.0, true, null, null
      );
      if (MovementUtil.method_363()
         && Hub.field_2602.method_984().method_9(500L)
         && this.boost.getValue()
         && !(Math.sqrt(mc.player.distanceToSqr(var2)) > 6.0)
         && !(var3 < 4.0)) {
         this.field_2198.reset();
      }
   }

   public void reset() {
      this.field_2199.method_1156().field_1419 = 0.0;
      this.field_2199.method_1156().field_1570 = 0.0;
      this.field_2199.method_1156().field_1571 = 4;
      if (this.mode.getValue() == StrafeType.ON_GROUND) {
         this.field_2199.method_1156().field_1571 = 2;
      }
   }

   public boolean method_525() {
      return PlayerUtil.method_29(mc.player) && !this.inLiquid.getValue();
   }

   public boolean method_526() {
      AABB var1 = mc.player.getBoundingBox();
      AABB var2 = new AABB(
            (double)mc.player.blockPosition().getX(),
            var1.minY,
            (double)mc.player.blockPosition().getZ(),
            (double)mc.player.blockPosition().getX() + 1.0,
            var1.maxY,
            (double)mc.player.blockPosition().getZ() + 1.0
         )
         .deflate(1e-07);
      return mc.level.collidesWithSuffocatingBlock(mc.player, var2) && this.pauseInBlocks.getValue();
   }

   public boolean method_678() {
      return field_1637 == null ? false : field_1637.method_639();
   }

   public boolean method_404() {
      return this.method_535()
         ? true
         : this.method_526() || this.method_525() || PlayerUtil.method_427() || mc.player.isFallFlying() || mc.player.isSpectator();
   }
}
