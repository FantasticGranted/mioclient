package me.mioclient.module.movement;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;
import me.mioclient.Hub;
import me.mioclient.enum_.FastFallMode;
import me.mioclient.enum_.StrafeType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RaycastUtil;
import me.mioclient.internal.VectorUtil;
import me.mioclient.mixin.ducks.DuckAbstractBlock;
import me.mioclient.mixin.ducks.DuckClientPlayerEntity;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.module.exploit.PhaseModule;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction.Axis;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class FastFallModule extends Module {
   public static SpeedModule field_1001 = Hub.field_2595.method_78(SpeedModule.class);
   public static HoleSnapModule field_4398 = Hub.field_2595.method_78(HoleSnapModule.class);
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static PhaseModule field_4399 = Hub.field_2595.method_78(PhaseModule.class);
   public Setting<FastFallMode> mode;
   public Setting<Float> height;
   public Setting<Float> speed;
   public Setting<Boolean> stopInLiquids;
   public Setting<Boolean> onlyHole;
   public Setting<Boolean> safeDisable;
   public boolean field_4406;
   public boolean field_4407;
   public boolean field_4408;
   public boolean field_4409;
   public boolean field_264;
   public boolean field_2197;
   public double field_4410;
   public double field_4411;
   public final Timer field_4412;

   public FastFallModule() {
      super("FastFall", "Allows you to get in holes with ease!", Category.MOVEMENT, "reversestep");
      Settings.initialize(this);
      this.field_4412 = new Timer();
   }

   @Override
   public void onToggle() {
      this.field_4406 = false;
      this.field_2197 = false;
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      if (this.safeDisable.getValue() && Hub.field_2605.method_224() && this.field_2197) {
         this.disable();
      } else if (this.method_1187() && (this.field_4409 || !this.onlyHole.getValue())) {
         if (!mc.player.onGround()) {
            if (this.field_4412.method_9(500L)) {
               this.field_4408 = !this.field_4408;
               this.field_4412.reset();
            }

            if (mc.player.getDeltaMovement().y < 0.0 && this.field_4407 && this.field_4408) {
               Hub.field_2617.method_2(this, 2F);
               this.field_2197 = true;
            } else {
               this.method_1188();
            }
         } else {
            this.field_4407 = true;
            Hub.field_2617.method_38(this);
            if (Hub.field_2615.method_1161() > 3) {
               this.field_4412.reset();
               this.field_4408 = true;
            }
         }
      } else {
         this.method_1188();
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      boolean var2 = MovementUtil.method_363() || field_4398.isToggled() && field_4398.method_403() != null;
      if ((this.field_4409 || !this.onlyHole.getValue()) && var2) {
         this.field_264 = false;
         if (((DuckClientPlayerEntity)(Object)mc.player).lastOnGround()
            && mc.player.getDeltaMovement().y() <= 0.0
            && this.method_1186()
            && !this.method_724()) {
            double var3 = mc.player.getY();
            AABB var5 = mc.player.getBoundingBox().expandTowards(0.0, (double)(-this.height.getValue()), 0.0).setMaxY(var3);
            if (mc.level.noCollision(mc.player, var5) && !mc.level.containsAnyLiquid(var5)) {
               return;
            }

            Iterable<net.minecraft.world.phys.shapes.VoxelShape> var6 = mc.level.getBlockCollisions(mc.player, var5);
            AtomicReference<Double> var7 = new AtomicReference<>((double)mc.level.getMinY());
            var6.forEach(var1x -> var7.set(Math.max((Double)var7.get(), var1x.max(Axis.Y))));
            if ((Double)var7.get() < mc.player.getY()) {
               double var8 = (Double)var7.get();
               Vec3 var10 = mc.player.getDeltaMovement();
               BlockPos var11 = BlockPos.containing(mc.player.getX(), var8, mc.player.getZ());
               boolean var12 = mc.level.isEmptyBlock(var11.offset(new Vec3i((int)var10.x, -1, (int)var10.z)))
                  || !Hub.field_2602.method_985().method_9(25L)
                  || this.mode.getValue() == FastFallMode.PLAIN;
               if (mc.player.getY() - var8 > 2.0 || var12) {
                  var8 = mc.player.getY() - 2.0;
                  this.field_264 = true;
                  float var13 = this.mode.getValue() == FastFallMode.PLAIN ? this.speed.getValue() : 1F;
                  if (var13 != 0.0F) {
                     mc.player.setDeltaMovement(var10.with(Axis.Y, (double)(-var13)));
                  }
               }

               if (!var12) {
                  mc.player.setPos(mc.player.getX(), var8, mc.player.getZ());
               }

               field_4399.method_362();
               this.field_2197 = true;
            }
         }
      }
   }

   @Subscribe
   public void method_2(MoveEvent var1) {
      this.field_4410 = (double)RaycastUtil.method_887();
      this.field_4411 = (double)RaycastUtil.method_886();
      this.field_4409 = Hub.field_2605.method_221(mc.player.blockPosition().below((int)this.field_4411));
      if (this.field_4409 || !this.onlyHole.getValue()) {
         double var2 = 0.3;
         boolean var4 = field_4398.isToggled() && field_4398.field_1179.method_9(500L) && field_4398.method_403() != null;
         if (this.method_724() && this.method_1186() && this.method_1184() && mc.player.onGround() || this.field_264 && !var4 && this.method_1185()) {
            VectorUtil.method_2(var1.method_1066(), var1.method_380() * var2, var1.method_395(), var1.method_396() * var2);
         }
      }
   }

   public boolean method_1184() {
      AABB var1 = mc.player.getBoundingBox();
      Vec3 var2 = var1.getCenter();
      double var3 = var1.minX;
      double var5 = var1.minZ;
      double var7 = var1.maxX;
      double var9 = var1.maxZ;
      HashMap var11 = new HashMap();
      var11.put(var2, new Vec3(var2.x, var2.y - 1.0, var2.z));
      var11.put(new Vec3(var3, var2.y, var5), new Vec3(var3, var2.y - 1.0, var5));
      var11.put(new Vec3(var7, var2.y, var5), new Vec3(var7, var2.y - 1.0, var5));
      var11.put(new Vec3(var3, var2.y, var9), new Vec3(var3, var2.y - 1.0, var9));
      var11.put(new Vec3(var7, var2.y, var9), new Vec3(var7, var2.y - 1.0, var9));

      for (Vec3 var13 : (Iterable<Vec3>)(Iterable<?>)(var11.keySet())) {
         BlockHitResult var14 = mc.level
            .clip(new ClipContext(var13, (Vec3)var11.get(var13), Block.COLLIDER, Fluid.NONE, mc.player));
         if (var14 != null && var14.getType() == Type.BLOCK) {
            return false;
         }
      }

      if (!this.method_1185()) {
         return false;
      } else {
         BlockState var15 = mc.level
            .getBlockState(
               BlockPos.containing(mc.player.getX(), mc.player.getY() - 1.0, mc.player.getZ())
            );
         return var15 == null || var15.getBlock() == Blocks.AIR;
      }
   }

   public boolean method_1185() {
      Vec3 var1 = mc.player.getDeltaMovement();
      double var2 = var1.x;
      double var4 = var1.z;
      var2 = Math.abs(var4) > Math.abs(var2) ? 0.0 : Math.signum(var2);
      var4 = Math.abs(var2) > Math.abs(var4) ? 0.0 : Math.signum(var4);
      BlockState var6 = mc.level
         .getBlockState(
            BlockPos.containing(
               mc.player.getX() + var2, mc.player.getY() - 1.0, mc.player.getZ() + var4
            )
         );
      return var6 != null ? ((DuckAbstractBlock)(Object)var6.getBlock()).isCollidable() : true;
   }

   public boolean method_1186() {
      return field_1001.isToggled() && field_1001.mode.getValue() != StrafeType.ON_GROUND && field_1001.mode.getValue() != StrafeType.VANILLA
         ? false
         : !mc.player.isShiftKeyDown()
            && !mc.player.isFallFlying()
            && !mc.player.isInWall()
            && (!PlayerUtil.method_29(mc.player) || !this.stopInLiquids.getValue())
            && Hub.field_2602.method_984().method_9(500L)
            && !(mc.level.getBlockState(BlockPos.containing(mc.player.position())).getBlock() instanceof BedBlock);
   }

   public boolean method_724() {
      return this.mode.getValue() == FastFallMode.STRICT;
   }

   public boolean method_1187() {
      return this.method_1186()
         && this.method_724()
         && this.field_4410 != 0.0
         && this.field_4411 != 0.0
         && this.field_4411 <= (double)this.height.getValue().floatValue();
   }

   public void method_1188() {
      if (this.method_724()) {
         this.field_4407 = false;
         Hub.field_2617.method_38(this);
      }
   }
}
