package me.mioclient.internal;

import io.netty.buffer.Unpooled;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckClientWorld;
import net.minecraft.client.multiplayer.prediction.BlockStatePredictionHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ServerboundAttackPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Pos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.PosRot;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket.Rot;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.network.protocol.game.ServerboundSetCarriedItemPacket;
import net.minecraft.network.protocol.game.ServerboundSwingPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.network.protocol.game.ServerboundUseItemPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Convenience builders that construct and send C2S play packets (interact, move,
 * hand-swing, action, slot select) to the server.
 */
public class PacketSender implements MinecraftAccess {
   public PacketSender() {
      super();
   }

   public static void method_2(InteractionHand var0, BlockHitResult var1) {
      method_2(new ServerboundUseItemOnPacket(var0, var1, method_1101()));
   }

   public static void method_2(InteractionHand var0) {
      method_2(var0, mc.player.getYRot(), mc.player.getXRot());
   }

   public static void method_2(InteractionHand var0, float var1, float var2) {
      method_2(new ServerboundUseItemPacket(var0, method_1101(), var1, var2));
   }

   public static void method_9(InteractionHand var0) {
      method_2(new ServerboundSwingPacket(var0));
   }

   public static void method_2(float var0, float var1, boolean var2) {
      method_2(new Rot(var0, var1, var2, false));
   }

   public static void method_2(float[] var0, boolean var1) {
      if (var0 != null && var0.length >= 2) {
         method_2(var0[0], var0[1], var1);
      }
   }

   public static void method_2(double var0, double var2, double var4, boolean var6) {
      method_2(new Pos(var0, var2, var4, var6, false));
   }

   public static void method_2(double var0, double var2, double var4, float var6, float var7, boolean var8) {
      method_2(new PosRot(var0, var2, var4, var6, var7, var8, false));
   }

   public static void method_36(int var0) {
      Entity var1 = mc.level.getEntity(var0);
      if (var1 != null) {
         ServerboundAttackPacket var2 = new ServerboundAttackPacket(var1.getId());
         method_2(var2);
      }
   }

   public static void method_2(Action var0, BlockPos var1, Direction var2) {
      method_2(new ServerboundPlayerActionPacket(var0, var1, var2, method_1101()));
   }

   public static void method_2(Entity var0, net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action var1) {
      method_2(var0, var1, 0);
   }

   public static void method_2(Entity var0, net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action var1, int var2) {
      method_2(new ServerboundPlayerCommandPacket(var0, var1, var2));
   }

   public static void method_1099() {
      method_2(mc.player, net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket.Action.START_FALL_FLYING, 0);
   }

   public static void method_1100() {
      method_2(new ServerboundPlayerActionPacket(Action.SWAP_ITEM_WITH_OFFHAND, BlockPos.ZERO, Direction.DOWN));
   }

   public static void method_14(int var0) {
      method_9(var0, false);
   }

   public static void method_9(int var0, boolean var1) {
      if (var1 || Hub.field_2611.method_843() != var0) {
         method_2(new ServerboundSetCarriedItemPacket(var0));
      }
   }

   public static void method_2(Packet<?> var0) {
      mc.player.connection.send(var0);
   }

   public static int method_1101() {
      BlockStatePredictionHandler var0 = ((DuckClientWorld)(Object)mc.level).getPendingUpdateManager().startPredicting();
      if (var0 != null) {
         var0.close();
      }

      return var0.currentSequence();
   }

   public static void method_9(Packet<?> var0) {
      if (var0 != null) {
         Connection var1 = mc.player.connection.getConnection();
         if (var1.isConnected()) {
            if (var1.channel.eventLoop().inEventLoop()) {
               var1.channel.writeAndFlush(var0);
            } else {
               var1.channel.eventLoop().execute(() -> var1.channel.writeAndFlush(var0));
            }
         }
      }
   }
}
