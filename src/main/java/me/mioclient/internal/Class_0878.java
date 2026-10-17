package me.mioclient.internal;

import com.mojang.authlib.GameProfile;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import me.mioclient.mixin.ducks.DuckLivingEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.phys.Vec3;

public class Class_0878 extends RemotePlayer {
   public final Set<PlayerModelPart> field_2784 = new HashSet<>();
   public PlayerSkin field_2785;
   public boolean field_2005;
   public Vec3 field_2786;

   public Class_0878(ClientLevel var1) {
      super(var1, new GameProfile(UUID.randomUUID(), ""));
   }

   public void aiStep() {
   }

   public void method_78(Player var1) {
      this.field_2786 = new Vec3(var1.getX(), var1.getY(), var1.getZ());
      super.restoreFrom(var1);
      this.copyPosition(var1);
      this.yRotO = var1.getYRot();
      this.setYRot(var1.getYRot());
      this.xRotO = var1.getXRot();
      this.setXRot(var1.getXRot());
      this.yBodyRot = var1.yBodyRot;
      this.yBodyRotO = var1.yBodyRot;
      this.yHeadRot = var1.yHeadRot;
      this.yHeadRotO = var1.yHeadRot;
      this.walkAnimation.speed = var1.walkAnimation.speed;
      this.walkAnimation.speedOld = var1.walkAnimation.speed;
      this.walkAnimation.position = var1.walkAnimation.position;
      this.deathTime = 0;
      // TODO26.1 this.deltaMovementOnPreviousTick = new Vec3(0.0, 0.0, 0.0);
      this.setDeltaMovement(0.0, 0.0, 0.0);
      this.setPose(var1.getPose());
      this.setShiftKeyDown(var1.isShiftKeyDown());
      if (var1 instanceof AbstractClientPlayer var2) {
         this.field_2785 = var2.getSkin();

         for (PlayerModelPart var6 : PlayerModelPart.values()) {
            if (var6 != PlayerModelPart.CAPE && var2.isModelPartShown(var6)) {
               this.field_2784.add(var6);
            }
         }
      }

      for (int var7 = 0; var7 < this.getInventory().getContainerSize(); var7++) {
         this.getInventory().setItem(var7, var1.getInventory().getItem(var7).copy());
      }

      this.setItemInHand(InteractionHand.MAIN_HAND, var1.getMainHandItem().copy());
      this.setItemInHand(InteractionHand.OFF_HAND, var1.getOffhandItem().copy());
      this.getInventory().selected = var1.getInventory().selected;
      ((DuckLivingEntity)(Object)this).mio$setLeaningPitch(((DuckLivingEntity)(Object)var1).mio$getLeaningPitch());
      ((DuckLivingEntity)(Object)this).mio$setLastLeaningPitch(((DuckLivingEntity)(Object)var1).mio$getLastLeaningPitch());
   }

   public void setPos(double x, double y, double z) {
      super.setPos(x, y, z);
      this.xOld = x;
      this.yOld = y;
      this.zOld = z;
   }

   public Vec3 method_805() {
      return this.field_2786;
   }

   public PlayerSkin getSkin() {
      return this.field_2785 != null ? this.field_2785 : super.getSkin();
   }

   public boolean isModelPartShown(PlayerModelPart modelPart) {
      return this.field_2784.contains(modelPart);
   }

   public boolean method_806() {
      return this.field_2005;
   }

   public void method_14(boolean var1) {
      this.field_2005 = var1;
   }
}
