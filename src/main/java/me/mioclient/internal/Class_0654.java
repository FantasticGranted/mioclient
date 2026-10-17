package me.mioclient.internal;

import me.mioclient.api.PlayerTickHook;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckLivingEntity;

public class Class_0654 implements MinecraftAccess {
   public static boolean field_2106;

   public Class_0654() {
      super();
   }

   public static void method_633() {
      field_2106 = true;
      int var0 = mc.player.getUseItemRemainingTicks();
      float var1 = mc.player.moveDist;
      float var2 = mc.player.walkAnimation.speed();
      float var3 = mc.player.avatarState().walkDist;
      float var4 = mc.player.avatarState().walkDistO;
      float var5 = mc.player.yBob;
      float var6 = mc.player.yBobO;
      float var7 = mc.player.xBob;
      float var8 = mc.player.xBobO;
      int var9 = ((DuckLivingEntity)(Object)mc.player).mio$getLastAttackedTicks();
      int var10 = mc.player.swingTime;
      float var11 = mc.player.oAttackAnim;
      float var12 = mc.player.attackAnim;
      ((PlayerTickHook)mc.player).superTick();
      ((PlayerTickHook)mc.player).resetEvent();
      ((DuckLivingEntity)(Object)mc.player).mio$setLastAttackedTicks(var9);
      ((DuckLivingEntity)(Object)mc.player).setItemUseTimeLeft(var0);
      mc.player.moveDist = var1;
      mc.player.walkAnimation.setSpeed(var2);
      mc.player.avatarState().walkDist = var3;
      mc.player.avatarState().walkDistO = var4;
      mc.player.yBob = var5;
      mc.player.yBobO = var6;
      mc.player.xBob = var7;
      mc.player.xBobO = var8;
      mc.player.swingTime = var10;
      mc.player.oAttackAnim = var11;
      mc.player.attackAnim = var12;
      ((PlayerTickHook)mc.player).sendMovementPacketsWrapper();
      ((PlayerTickHook)mc.player).resetRotations();
      field_2106 = false;
   }

   public static boolean method_663() {
      return field_2106;
   }
}
