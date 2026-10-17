package me.mioclient.module.movement;

import java.util.concurrent.TimeUnit;
import me.mioclient.Hub;
import me.mioclient.api.BlockFilter;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.MovementInputEvent;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.Class_0444;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.FilteredRaycastContext;
import me.mioclient.internal.RaycastUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.Pair;
import me.mioclient.record.SafetyBox;
import me.mioclient.setting.Setting;
import net.minecraft.core.Direction.Axis;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;
import net.minecraft.world.phys.Vec2;
import me.mioclient.internal.Inputs;

public class HoleSnapModule extends Module {
   public Setting<Boolean> autoDisable;
   public Setting<Boolean> directional;
   public Setting<Boolean> shift;
   public Setting<Boolean> pauseStep;
   public Setting<Float> height;
   public Setting<Float> timeout;
   public Setting<Float> range;
   public Setting<Float> speed;
   public Setting<Float> pitch;
   public final Class_0444 field_1177;
   public final Timer field_1178;
   public final Timer field_1179;
   public boolean field_1180;
   public boolean field_1181;

   public HoleSnapModule() {
      super("HoleSnap", "Pushes you into holes as you go past them.", Category.MOVEMENT, "holetp", "anchor");
      Settings.initialize(this);
      this.field_1177 = new Class_0444(this);
      this.field_1178 = new Timer();
      this.field_1179 = new Timer();
      this.field_1181 = false;
      this.pitch.method_2("None", SettingMode.MIN);
   }

   @Override
   public void onToggle() {
      this.field_1179.setTime(-1L);
   }

   @Subscribe
   public void method_5(Event_7 var1) {
      boolean var2 = Hub.field_2605.method_224();
      if (mc.player.onGround() && var2 != this.field_1180) {
         this.field_1180 = var2;
         this.field_1178.reset();
      }

      if (!this.method_404() && this.field_1179.method_2((double)this.timeout.getValue().floatValue(), TimeUnit.SECONDS)) {
         Pair var3 = this.method_403();
         if (var3 == null) {
            this.field_1181 = false;
            Hub.field_2617.method_38(this);
         } else {
            if (RotationManager.method_513()) {
               Hub.field_2598.method_2(RotationManager.method_78((Vec3)var3.getLeft()), 1337, true);
               this.field_1181 = true;
            }

            Class_0444.field_1422 = this.field_1177.method_484();
            if (!Class_0444.field_1422) {
               Hub.field_2617.method_38(this);
            }
         }
      } else {
         Hub.field_2617.method_38(this);
      }
   }

   @Subscribe
   public void method_2(MoveEvent var1) {
      if (!this.method_404()) {
         if (Hub.field_2605.method_221(PlayerUtil.method_425())) {
            if (this.autoDisable.getValue()) {
               this.disable();
               this.field_1181 = false;
               return;
            }

            this.field_1179.reset();
         }

         if (this.field_1179.method_2((double)this.timeout.getValue().floatValue(), TimeUnit.SECONDS) && !RotationManager.method_513()) {
            Pair var2 = this.method_403();
            if (var2 == null) {
               this.field_1181 = false;
            } else {
               Vec3 var3 = ((Vec3)var2.getLeft())
                  .subtract(mc.player.position())
                  .normalize()
                  .scale(Math.min(MovementUtil.method_78(true) * (double)this.speed.getValue().floatValue(), (Double)var2.getRight()));
               var1.method_7(var3.x, var3.z);
            }
         }
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_2(MovementInputEvent var1) {
      if (this.field_1181 && !this.method_404() && this.field_1179.method_2((double)this.timeout.getValue().floatValue(), TimeUnit.SECONDS)) {
         var1.method_276().moveVector = new Vec2(var1.method_276().moveVector.x, var1.method_278() ? var1.method_277() : 1F);
         Inputs.setForward(var1.method_276(), true);
      }
   }

   @Subscribe
   public void method_9(TravelEvent var1) {
      if (this.field_1181 && !this.method_404() && this.field_1179.method_2((double)this.timeout.getValue().floatValue(), TimeUnit.SECONDS)) {
         Pair var2 = this.method_403();
         if (var2 == null) {
            return;
         }

         float[] var3 = RotationManager.method_78((Vec3)var2.getLeft());
         var1.setYaw(var3[0]);
         var1.setPitch(var3[1]);
         var1.method_463();
      }
   }

   public Pair<Vec3, Double> method_403() {
      double var1 = 9999.0;
      Vec3 var3 = null;
      Vec3 var4 = mc.player.position();

      for (SafetyBox var6 : Hub.field_2605.method_223()) {
         Vec3 var7 = var6.getBox().getCenter();
         var7 = var7.with(
            Axis.Y,
            Mth.clamp(var4.y(), var7.y, var7.y + (double)this.height.getValue().floatValue() - 0.5)
         );
         if (!((double)var6.getPos().getY() >= var4.y()) && !var6.method_676()) {
            double var8 = var4.distanceTo(var7);
            if ((!this.directional.getValue() || MovementUtil.method_2(var7) || !(var8 > 0.05))
               && !(var8 > (double)this.range.getValue().floatValue())
               && mc.level.noCollision(var6.getBox().setMaxY(mc.player.getBoundingBox().maxY))
               && !this.method_29(var4, var7)
               && !this.method_29(
                  var4.add(0.0, 1.7999999523162842, 0.0), var7.add(0.0, 1.7999999523162842, 0.0)
               )
               && var8 < var1) {
               var1 = var8;
               var3 = var7;
            }
         }
      }

      return var1 > (double)this.range.getValue().floatValue() ? null : new Pair<>(var3, var1);
   }

   public boolean method_404() {
      if (mc.player.isCrouching()) {
         return true;
      } else {
         return mc.player.getXRot() < this.pitch.getValue() && this.pitch.getValue() != 0.0F
            ? true
            : !Hub.field_2602.method_984().method_9(250L) || mc.player.isSpectator() || mc.player.isFallFlying();
      }
   }

   public boolean method_29(Vec3 var1, Vec3 var2) {
      FilteredRaycastContext var3 = new FilteredRaycastContext(var1, var2, Block.COLLIDER, Fluid.NONE, mc.player, BlockFilter.field_2968);
      return RaycastUtil.method_2(var3).getType() != Type.MISS;
   }

   public boolean method_405() {
      return !this.field_1178.method_9(400L) && Hub.field_2605.method_224() && this.pauseStep.getValue();
   }
}
