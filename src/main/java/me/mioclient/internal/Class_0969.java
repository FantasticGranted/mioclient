package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.CameraAccess;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.render.TrajectoriesModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.entity.projectile.hurtingprojectile.DragonFireball;
import net.minecraft.world.entity.projectile.hurtingprojectile.LargeFireball;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.throwableitemprojectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEgg;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownExperienceBottle;
import net.minecraft.world.entity.projectile.arrow.ThrownTrident;
import net.minecraft.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.EggItem;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.item.ThrowablePotionItem;
import net.minecraft.world.item.TridentItem;
import net.minecraft.world.item.component.ChargedProjectiles;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.HitResult.Type;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Class_0969 implements MinecraftAccess {
   public static TrajectoriesModule field_2978 = Hub.field_2595.method_78(TrajectoriesModule.class);
   public static final HitResult field_2979 = new HitResult(Vec3.ZERO) {
      public Type getType() {
         return Type.MISS;
      }
   };
   public static final MutableBlockPos field_2980 = new MutableBlockPos();
   public static Vec3 field_2981 = new Vec3(0.0, 0.0, 0.0);
   public static Vec3 field_2982 = new Vec3(0.0, 0.0, 0.0);
   public final Vector3d field_2983 = new Vector3d();
   public final Vector3d field_2984 = new Vector3d();
   public double field_2985;
   public double field_2986;
   public double field_2987;

   public Class_0969() {
      super();
   }

   public boolean method_2(Entity var1, ItemStack var2, double var3, boolean var5, double var6) {
      Item var8 = var2.getItem();
      if (var8 instanceof BowItem && field_2978.bow.getValue()) {
         double var11 = Mth.lerp(var6, (double)field_2978.method_711(), (double)BowItem.getPowerForTime(mc.player.getTicksUsingItem()));
         if (var11 <= 0.0) {
            var11 = 1.0;
         }

         this.method_2(
            var1,
            0.0,
            var11 * 3.0,
            var3,
            0.05000000074505806,
            0.6,
            var5,
            var6
         );
      } else if (var8 instanceof CrossbowItem && field_2978.xBow.getValue()) {
         if (!CrossbowItem.isCharged(var2)) {
            return false;
         }

         ChargedProjectiles var9 = (ChargedProjectiles)var2.get(DataComponents.CHARGED_PROJECTILES);
         if (var9 == null) {
            return false;
         }

         if (var9.contains(Items.FIREWORK_ROCKET)) {
            this.method_2(var1, 0.0, (double)CrossbowItem.getShootingPower(var9), var3, 0.0, 0.6, var5, var6);
         } else {
            this.method_2(
               var1,
               0.0,
               (double)CrossbowItem.getShootingPower(var9),
               var3,
               0.05000000074505806,
               0.6,
               var5,
               var6
            );
         }
      } else if (var8 instanceof FishingRodItem && field_2978.others.getValue()) {
         this.method_5(var1, var6);
      } else if (var8 instanceof TridentItem && field_2978.trident.getValue()) {
         this.method_2(
            var1,
            0.0,
            2.5,
            var3,
            0.05000000074505806,
            0.99,
            var5,
            var6
         );
      } else if ((!(var8 instanceof SnowballItem) && !(var8 instanceof EggItem) || !field_2978.others.getValue())
         && (!(var8 instanceof EnderpearlItem) || !field_2978.pearls.getValue())) {
         if (var8 instanceof ExperienceBottleItem && field_2978.exp.getValue()) {
            this.method_2(
               var1,
               -2e+01,
               0.7,
               var3,
               0.07,
               0.8,
               var5,
               var6
            );
         } else {
            if (!(var8 instanceof ThrowablePotionItem) || !field_2978.others.getValue()) {
               return false;
            }

            this.method_2(
               var1,
               -2e+01,
               Class_0245.field_688,
               var3,
               0.05,
               0.8,
               var5,
               var6
            );
         }
      } else {
         this.method_2(
            var1,
            0.0,
            1.5,
            var3,
            0.03,
            0.8,
            var5,
            var6
         );
      }

      return true;
   }

   public void method_2(Entity var1, double var2, double var4, double var6, double var8, double var10, boolean var12, double var13) {
      this.field_2983
         .set(
            Mth.lerp(var13, var1.xo, var1.getX()),
            Mth.lerp(var13, var1.yo, var1.getY()) + (double)var1.getEyeHeight(var1.getPose()),
            Mth.lerp(var13, var1.zo, var1.getZ())
         );
      double var15 = Mth.lerp(var13, (double)var1.yRotO, (double)var1.getYRot());
      double var17 = Mth.lerp(var13, (double)var1.xRotO, (double)var1.getXRot());
      if (var1 == mc.player) {
         var15 = Mth.lerp(var13, (double)Hub.field_2598.method_518(), (double)Hub.field_2598.method_515());
         var17 = Mth.lerp(var13, (double)Hub.field_2598.method_519(), (double)Hub.field_2598.method_516());
      }

      double var19;
      double var21;
      double var23;
      if (var6 == 0.0) {
         var19 = -Math.sin(var15 * (double)Class_0245.field_690) * Math.cos(var17 * (double)Class_0245.field_690);
         var21 = -Math.sin((var17 + var2) * (double)Class_0245.field_690);
         var23 = Math.cos(var15 * (double)Class_0245.field_690) * Math.cos(var17 * (double)Class_0245.field_690);
      } else {
         Vec3 var25 = var1.getUpVector(1F);
         Quaterniond var26 = new Quaterniond().setAngleAxis(var6, var25.x, var25.y, var25.z);
         Vec3 var27 = var1.getViewVector(1F);
         Vector3d var28 = new Vector3d(var27.x, var27.y, var27.z);
         var28.rotate(var26);
         var19 = var28.x;
         var21 = var28.y;
         var23 = var28.z;
      }

      this.field_2984.set(var19, var21, var23).normalize().mul(var4);
      if (var12) {
         Vec3 var29 = var1.getDeltaMovement();
         if (mc.player == var1) {
            this.field_2984
               .add(
                  -Mth.lerp(var13, field_2978.method_710().x, var29.x),
                  var1.onGround() ? 0.0 : Mth.lerp(var13, field_2978.method_710().y, var29.y),
                  -Mth.lerp(var13, field_2978.method_710().z, var29.z)
               );
         } else {
            this.field_2984.add(-var29.x, var1.onGround() ? 0.0 : var29.y, -var29.z);
         }
      }

      this.field_2985 = var8;
      this.field_2986 = 0.99;
      this.field_2987 = var10;
   }

   public boolean method_2(Entity var1, boolean var2, double var3) {
      if (!mc.level.noCollision(var1, var1.getBoundingBox().inflate(BoxUtil.field_2280))) {
         return false;
      } else {
         if (var1 instanceof Arrow var5) {
            this.method_2(
               var1, var5.getDeltaMovement().length(), 0.05000000074505806, 0.6, var2, var3
            );
         } else if (var1 instanceof ThrownEnderpearl || var1 instanceof Snowball || var1 instanceof ThrownEgg) {
            this.method_2(
               var1,
               1.5,
               0.03,
               0.8,
               var2,
               var3
            );
            if (BlockPos.betweenClosedStream(var1.getBoundingBox().inflate(0.0, 2.0, 0.0))
               .<BlockPos>map(BlockPos::immutable)
               .anyMatch(var0 -> mc.level.getBlockState(var0).getBlock() == Blocks.BUBBLE_COLUMN)) {
               Vec3 var6 = var1.getDeltaMovement();
               this.method_2(
                  var1,
                  new Vec3(var6.x, -999.0, var6.z),
                  1.0,
                  1.5
               );
            }
         } else if (var1 instanceof ThrownTrident) {
            this.method_2(
               var1,
               2.5,
               0.05000000074505806,
               0.99,
               var2,
               var3
            );
         } else if (var1 instanceof ThrownExperienceBottle) {
            this.method_2(
               var1,
               0.7,
               0.07,
               0.8,
               var2,
               var3
            );
         } else if (var1 instanceof ThrowableProjectile) {
            this.method_2(var1, Class_0245.field_688, 0.05, 0.8, var2, var3);
         } else {
            if (!(var1 instanceof WitherSkull) && !(var1 instanceof LargeFireball) && !(var1 instanceof DragonFireball)) {
               return false;
            }

            this.method_2(var1, 0.95, 0.0, 0.8, var2, var3);
         }

         if (var1.isNoGravity()) {
            this.field_2985 = 0.0;
         }

         return true;
      }
   }

   public void method_2(Entity var1, double var2, double var4, double var6, boolean var8, double var9) {
      this.field_2983
         .set(Mth.lerp(var9, var1.xo, var1.getX()), Mth.lerp(var9, var1.yo, var1.getY()), Mth.lerp(var9, var1.zo, var1.getZ()));
      this.method_2(var1, var1.getDeltaMovement(), var9, var2);
      if (var8) {
         Vec3 var11 = var1.getDeltaMovement();
         this.field_2984.add(var11.x, var1.onGround() ? 0.0 : var11.y, var11.z);
      }

      this.field_2985 = var4;
      this.field_2986 = 0.99;
      this.field_2987 = var6;
   }

   public void method_2(Entity var1, Vec3 var2, double var3, double var5) {
      CameraAccess var7 = (CameraAccess)var1;
      this.field_2984
         .set(
            Mth.lerp(var3, var7.mio$getPrevVelocity().x, var2.x),
            Mth.lerp(var3, var7.mio$getPrevVelocity().y, var2.y),
            Mth.lerp(var3, var7.mio$getPrevVelocity().z, var2.z)
         )
         .normalize()
         .mul(var5);
   }

   public void method_5(Entity var1, double var2) {
      double var4 = Mth.lerp(var2, (double)var1.yRotO, (double)var1.getYRot());
      double var6 = Mth.lerp(var2, (double)var1.xRotO, (double)var1.getXRot());
      double var8 = Math.cos(-var4 * (double)Class_0245.field_690 - 3.1415927410125732);
      double var10 = Math.sin(-var4 * (double)Class_0245.field_690 - 3.1415927410125732);
      double var12 = -Math.cos(-var6 * (double)Class_0245.field_690);
      double var14 = Math.sin(-var6 * (double)Class_0245.field_690);
      this.field_2983
         .set(
            Mth.lerp(var2, var1.xo, var1.getX()),
            Mth.lerp(var2, var1.yo, var1.getY()) + (double)var1.getEyeHeight(var1.getPose()),
            Mth.lerp(var2, var1.zo, var1.getZ())
         );
      this.field_2983.sub(var10 * 0.3, 0.0, var8 * 0.3);
      this.field_2984
         .set(-var10, Mth.clamp(-(var14 / var12), -5.0, 5.0), -var8);
      double var16 = this.field_2984.length();
      this.field_2984
         .mul(
            0.6 / var16 + Class_0245.field_688,
            0.6 / var16 + Class_0245.field_688,
            0.6 / var16 + Class_0245.field_688
         );
      this.field_2985 = 0.03;
      this.field_2986 = 0.92;
      this.field_2987 = 0.0;
   }

   public HitResult method_870() {
      field_2982 = new Vec3(this.field_2983.x, this.field_2983.y, this.field_2983.z);
      this.field_2983.add(this.field_2984);
      this.field_2984.mul(this.method_871() ? this.field_2987 : this.field_2986);
      this.field_2984.sub(0.0, this.field_2985, 0.0);
      if (this.field_2983.y < (double)mc.level.getMinY()) {
         return field_2979;
      } else {
         int var1 = (int)(this.field_2983.x / 16.0);
         int var2 = (int)(this.field_2983.z / 16.0);
         if (!mc.level.getChunkSource().hasChunk(var1, var2)) {
            return field_2979;
         } else {
            field_2981 = new Vec3(this.field_2983.x, this.field_2983.y, this.field_2983.z);
            HitResult var3 = this.method_872();
            return var3.getType() == Type.MISS ? null : var3;
         }
      }
   }

   public boolean method_871() {
      field_2980.set(this.field_2983.x, this.field_2983.y, this.field_2983.z);
      FluidState var1 = mc.level.getFluidState(field_2980);
      return var1.getType() != Fluids.WATER && var1.getType() != Fluids.FLOWING_WATER
         ? false
         : this.field_2983.y - (double)((int)this.field_2983.y) <= (double)var1.getOwnHeight();
   }

   public HitResult method_872() {
      Vec3 var1 = field_2982;
      HitResult var2 = mc.level
         .clip(new ClipContext(var1, field_2981, Block.COLLIDER, this.field_2987 == 0.0 ? Fluid.ANY : Fluid.NONE, mc.player));
      if (var2.getType() != Type.MISS) {
         var1 = var2.getLocation();
      }

      EntityHitResult var3 = ProjectileUtil.getEntityHitResult(
         mc.level,
         mc.player,
         var1,
         field_2981,
         new AABB(this.field_2983.x, this.field_2983.y, this.field_2983.z, this.field_2983.x, this.field_2983.y, this.field_2983.z)
            .expandTowards(mc.player.getDeltaMovement())
            .inflate(1.0),
         var0 -> !var0.isSpectator() && var0.isAlive() && var0.isPickable()
      , 1.0E-4F);
      if (var3 != null) {
         var2 = var3;
      }

      return (HitResult)var2;
   }
}