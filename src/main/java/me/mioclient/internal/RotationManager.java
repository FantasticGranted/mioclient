package me.mioclient.internal;

import java.util.ArrayDeque;
import java.util.Queue;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.Class_1054;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.mixin.ducks.DuckClientPlayerEntity;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.record.RotationRequest;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

/**
 * Central rotation/aim manager: queues rotation requests by priority, applies them to
 * outgoing move packets (server-side/silent rotations) and handles movement-sync capture.
 */
public final class RotationManager implements MinecraftAccess {
   public static AbstractModule_28 field_144 = Hub.field_2595.method_78(AbstractModule_28.class);
   public static boolean field_1538;
   public float yaw;
   public float pitch;
   public float bodyYaw;
   public float field_1542;
   public float field_1543;
   public float prevBodyYaw;
   public float prevHeadYaw;
   public float headYaw;
   public float[] lastRotations;
   public int lastAge;
   public int repeatCount;
   public final Queue<Packet<?>> field_800 = new ArrayDeque<>();
   public boolean capturing;
   public boolean replaying;
   public RotationRequest pendingRotation;
   public boolean forced;

   public RotationManager() {
      super();
      lookups.method_14(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.forced = false;
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      if (method_513()) {
         this.method_507();
         this.method_508();
      }
   }

   @Subscribe
   public void method_2(TravelEvent var1) {
      boolean var2 = field_144.movementSync.getValue() || this.forced;
      if (this.pendingRotation != null && var2) {
         float[] var3 = this.pendingRotation.getRotations();
         float var4 = method_7(var3);
         if (var4 != var3[0]
            && var4 != Mth.wrapDegrees(var3[0] - (float)Class_0245.field_684)
            && var4 != Mth.wrapDegrees(var3[0] + (float)Class_0245.field_684)) {
            mc.player.setSprinting(false);
         }

         var1.method_463();
         var1.setYaw(var4);
         var1.setPitch(var3[1]);
      }
   }

   public static float method_7(float[] var0) {
      if (mc.player.isFallFlying()) {
         return var0[0];
      } else {
         float var1 = (float)Class_0245.field_686;
         float var2 = var0[0];

         for (int var3 = 0; var3 < Class_0245.field_686; var3 += Class_0245.field_685) {
            float var4 = Mth.wrapDegrees(var0[0] + (float)var3);
            float var5 = Mth.degreesDifferenceAbs(var4, mc.player.getYRot());
            if (var5 <= var1) {
               var2 = var4;
               var1 = var5;
            }
         }

         return var2;
      }
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (this.repeatCount > 0 && this.lastRotations != null) {
         this.method_29(this.lastRotations);
         this.repeatCount--;
      }

      if (var1.method_213() == PreType.POST) {
         if (!method_513()) {
            this.method_507();
         }
      } else {
         if (!method_513()) {
            this.method_508();
         }

         if (this.pendingRotation != null) {
            var1.method_5(this.pendingRotation.getRotations());
            this.pendingRotation = null;
         }
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (this.capturing && !var1.method_464() && !this.replaying) {
         if (var1.method_127() instanceof ServerboundMovePlayerPacket) {
            var1.method_463();
            PacketSender.method_9(var1.method_127());
            this.method_507();
         } else {
            this.field_800.add(var1.method_127());
            var1.method_463();
         }
      }
   }

   public boolean method_2(Action var1) {
      return false;
   }

   public void method_209(boolean var1) {
      this.capturing = var1;
   }

   public boolean method_506() {
      return this.capturing;
   }

   public void method_507() {
      long var1 = 0L;

      while (!this.field_800.isEmpty()) {
         if (++var1 > 500L) {
            System.out.println("anticrash1337");
            break;
         }

         Packet var3 = this.field_800.poll();
         if (var3 != null) {
            this.replaying = true;
            PacketSender.method_2(var3);
            this.replaying = false;
         }
      }

      this.field_800.clear();
   }

   public void method_508() {
      this.capturing = true;
      lookups.method_36(new Event_7());
      this.capturing = false;
      if (this.pendingRotation == null) {
         this.method_507();
      }
   }

   public float method_509() {
      return field_144.method_1051();
   }

   public RotationRequest method_510() {
      return this.pendingRotation;
   }

   public void method_2(RotationRequest var1) {
      if (this.pendingRotation == null || this.pendingRotation.method_800() <= var1.method_800()) {
         this.pendingRotation = var1;
      }
   }

   public void method_2(float[] var1, int var2) {
      this.method_2(var1, var2, false);
   }

   public void method_2(float[] var1, int var2, boolean var3) {
      if (!field_144.method_1052() || !(mc.screen instanceof AbstractContainerScreen)) {
         if (!method_514() || var3) {
            this.method_2(new RotationRequest(var1, var2));
         } else if (this.capturing) {
            this.method_29(var1);
            PacketSender.method_2(mc.player.getX(), mc.player.getY(), mc.player.getZ(), var1[0], var1[1], Hub.field_2615.method_707());
         }
      }
   }

   public void method_511() {
      this.forced = true;
   }

   public void method_29(float[] var1) {
      this.method_9(var1, 0);
   }

   public void method_9(float[] var1, int var2) {
      this.lastAge = mc.player.tickCount;
      this.field_1542 = this.yaw;
      this.field_1543 = this.pitch;
      this.prevBodyYaw = this.bodyYaw;
      this.bodyYaw = this.method_4(var1[0], this.prevBodyYaw);
      this.prevHeadYaw = this.headYaw;
      this.headYaw = var1[0];
      this.yaw = var1[0];
      this.pitch = var1[1];
      this.lastRotations = var1;
      if (var2 > 0) {
         this.repeatCount = var2;
      }
   }

   public static float[] method_98(BlockPos var0) {
      return method_78(
         new Vec3((double)var0.getX() + Class_0245.field_688, (double)var0.getY() + Class_0245.field_688, (double)var0.getZ() + Class_0245.field_688)
      );
   }

   public static float[] method_14(Entity var0) {
      return method_78(BoxUtil.method_9(mc.player.getEyePosition(), var0.getBoundingBox()));
   }

   public static float[] method_78(Vec3 var0) {
      return method_7(mc.player.getEyePosition(RenderUtil.method_776()), var0);
   }

   public static float[] method_2(Vec3 var0, Direction var1) {
      return method_7(mc.player.getEyePosition(RenderUtil.method_776()), var1 == null ? var0 : var0.relative(var1, Class_0245.field_688));
   }

   public static float[] method_7(Vec3 var0, Vec3 var1) {
      double var2 = var1.x - var0.x;
      double var4 = (var1.y - var0.y) * -1.0;
      double var6 = var1.z - var0.z;
      double var8 = Math.sqrt(var2 * var2 + var6 * var6);
      return new float[]{
         (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(var6, var2)) - (double)Class_0245.field_685),
         (float)Mth.wrapDegrees(Math.toDegrees(Math.atan2(var4, var8)))
      };
   }

   public static float[] method_2(float[] var0, float var1) {
      if (var0 == null) {
         return null;
      } else {
         var1 = Mth.clamp(var1, 0.1F, 1F);
         if (var1 < 1F) {
            float var2 = ((DuckClientPlayerEntity)(Object)mc.player).lastYaw();
            float var3 = Mth.wrapDegrees(var0[0] - var2);
            if (Math.abs(var3) > 1.8e+02F * var1) {
               var0[0] = var2 + var3 * (1.8e+02F * var1 / Math.abs(var3));
            }
         }

         return new float[]{var0[0], var0[1]};
      }
   }

   public static int method_221(float var0) {
      return Mth.floor((double)(var0 * 8F / (float)Class_0245.field_686) + Class_0245.field_688) & 7;
   }

   public static int method_512() {
      return method_222(mc.gameRenderer.getMainCamera().yRot());
   }

   public static int method_222(float var0) {
      return Mth.floor((double)(var0 * 4F / (float)Class_0245.field_686) + Class_0245.field_688) & 3;
   }

   public static Direction method_220(int var0) {
      return switch (var0) {
         case 0 -> Direction.SOUTH;
         case 1 -> Direction.WEST;
         case 2 -> Direction.NORTH;
         case 3 -> Direction.EAST;
         default -> Direction.UP;
      };
   }

   public static boolean method_4(AABB var0) {
      Frustum var1 = mc.gameRenderer.getMainCamera().getCullFrustum();
      return var1 == null || var1.isVisible(var0);
   }

   public static boolean method_39(Vec3 var0) {
      return method_4(new AABB(BlockPos.containing(var0)));
   }

   public void method_29(float var1, float var2) {
      if (mc.player.tickCount != this.lastAge) {
         this.method_29(new float[]{var1, var2});
      }
   }

   public static boolean method_513() {
      return field_144 == null ? false : field_144.movementSync.getValue();
   }

   public static boolean method_514() {
      return field_144 == null ? false : method_513() && field_144.rotations.getValue() == Class_1054.SILENT;
   }

   public float method_515() {
      return this.yaw;
   }

   public float method_516() {
      return this.pitch;
   }

   public float method_517() {
      return this.bodyYaw;
   }

   public float method_518() {
      return this.field_1542;
   }

   public float method_519() {
      return this.field_1543;
   }

   public float method_520() {
      return this.prevBodyYaw;
   }

   public float method_521() {
      return this.prevHeadYaw;
   }

   public float method_522() {
      return this.headYaw;
   }

   public int method_523() {
      return this.lastAge;
   }

   public float method_4(float var1, float var2) {
      if (mc.player.isPassenger()) {
         return var1;
      } else {
         float var3 = var2;
         double var5 = mc.player.getX() - mc.player.xo;
         double var7 = mc.player.getZ() - mc.player.zo;
         if (var5 * var5 + var7 * var7 > 0.002500000176951289) {
            float var4 = (float)Mth.atan2(var7, var5) * 57.295776F - (float)Class_0245.field_685;
            float var9 = Mth.abs(Mth.wrapDegrees(var1) - var4);
            if (95F < var9 && var9 < 265F) {
               var3 = var4 - 1.8e+02F;
            } else {
               var3 = var4;
            }
         }

         if (mc.player.attackAnim > 0.0F) {
            var3 = var1;
         }

         var3 = var2 + Mth.wrapDegrees(var3 - var2) * 0.3F;
         float var12 = Mth.wrapDegrees(var1 - var3);
         if (var12 < -75F) {
            var12 = -75F;
         } else if (var12 >= 75F) {
            var12 = 75F;
         }

         var3 = var1 - var12;
         if (var12 * var12 > 2.5e+03F) {
            var3 += var12 * 0.2F;
         }

         return var3;
      }
   }
}
