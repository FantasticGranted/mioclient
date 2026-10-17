package me.mioclient.enum_;

import java.util.HashSet;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Class_0245;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.movement.NoFallModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

/** NoFall technique (packet-based). */
public enum NoFallMode implements Nameable {
   PACKET("Packet") {
      @Override
      public void method_2(PacketSendEvent var1) {
         if (var1.method_127() instanceof ServerboundMovePlayerPacket var2
            && MinecraftAccess.mc.player.fallDistance > 3.0F
            && !MinecraftAccess.mc.player.isFallFlying()) {
            ((DuckPlayerMoveC2SPacket)(Object)var2).setOnGround(true);
         }
      }
   },
   ANTI("Anti") {
      @Override
      public void method_2(PacketSendEvent var1) {
         if (var1.method_127() instanceof ServerboundMovePlayerPacket var2
            && MinecraftAccess.mc.player.fallDistance > 3.0F
            && !MinecraftAccess.mc
               .level
               .noCollision(MinecraftAccess.mc.player.getBoundingBox().expandTowards(0.0, MinecraftAccess.mc.player.getDeltaMovement().y * 1.1, 0.0))) {
            MinecraftAccess.mc
               .player
               .setPosRaw(MinecraftAccess.mc.player.getX(), MinecraftAccess.mc.player.getY() + 2.0, MinecraftAccess.mc.player.getZ());
         }
      }
   },
   TELEPORT("Teleport") {
      @Override
      public void method_2(NoFallModule var1) {
         if (MinecraftAccess.mc.player.fallDistance > 3.0F) {
            HashSet var2 = var1.method_78(MinecraftAccess.mc.player.getBoundingBox());
            var2.add(BlockPos.containing(MinecraftAccess.mc.player.position()));

            for (int var3 = 0; (float)var3 <= var1.groundDistance.getValue(); var3++) {
               for (BlockPos var5 : (Iterable<BlockPos>)(Iterable<?>)(var2)) {
                  BlockState var6 = MinecraftAccess.mc.level.getBlockState(var5.relative(Direction.DOWN, var3));
                  if (var6.getBlock() != Blocks.AIR) {
                     if (BlockUtil.method_1071().isEnd()) {
                        PacketSender.method_2(0.0, 64.0, 0.0, false);
                     } else {
                        PacketSender.method_2(MinecraftAccess.mc.player.getX(), 0.0, MinecraftAccess.mc.player.getZ(), false);
                     }

                     MinecraftAccess.mc.player.fallDistance = 0.0F;
                  }
               }
            }
         }
      }
   },
   MLG("MLG") {
      @Override
      public void method_2(NoFallModule var1) {
         if (MinecraftAccess.mc.player.fallDistance > 3.0F) {
            HashSet var2 = var1.method_78(MinecraftAccess.mc.player.getBoundingBox());
            var2.add(BlockPos.containing(MinecraftAccess.mc.player.position()));

            for (int var3 = 0; (float)var3 <= var1.groundDistance.getValue(); var3++) {
               for (BlockPos var5 : (Iterable<BlockPos>)(Iterable<?>)(var2)) {
                  BlockState var6 = MinecraftAccess.mc.level.getBlockState(var5.relative(Direction.DOWN, var3));
                  if (var6.getBlock() != Blocks.AIR) {
                     boolean var7 = MinecraftAccess.mc.player.getOffhandItem().getItem() == Items.WATER_BUCKET;
                     int var8 = InventoryUtil.method_5(Items.WATER_BUCKET);
                     if (var8 != -1 || var7) {
                        MinecraftAccess.mc.player.setXRot((float)Class_0245.field_685);
                        if (!var7) {
                           InventoryUtil.method_16(var8);
                        }

                        InteractionHand var9 = var7 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
                        MinecraftAccess.mc.gameMode.useItem(MinecraftAccess.mc.player, var9);
                        MinecraftAccess.mc.player.swing(var9);
                     }
                  }
               }
            }
         }
      }
   },
   GRIM("Grim") {
      @Override
      public void method_2(NoFallModule var1) {
         if (!var1.method_535() && !var1.method_1147() && !MinecraftAccess.mc.player.onGround() && MinecraftAccess.mc.player.fallDistance > 3.0F) {
            HashSet var2 = var1.method_78(MinecraftAccess.mc.player.getBoundingBox());
            var2.add(BlockPos.containing(MinecraftAccess.mc.player.position()));

            for (int var3 = 0; var3 <= 3; var3++) {
               for (BlockPos var5 : (Iterable<BlockPos>)(Iterable<?>)(var2)) {
                  BlockState var6 = MinecraftAccess.mc.level.getBlockState(var5.relative(Direction.DOWN, var3));
                  if (var6.getBlock() != Blocks.AIR) {
                     PacketSender.method_2(
                        MinecraftAccess.mc.player.getX(),
                        MinecraftAccess.mc.player.getY() + 1.0E-9,
                        MinecraftAccess.mc.player.getZ(),
                        MinecraftAccess.mc.player.getYRot(),
                        MinecraftAccess.mc.player.getXRot(),
                        false
                     );
                  }
               }
            }
         }
      }
   };

   public final String field_411;

    NoFallMode(String var3) {
      this.field_411 = var3;
   }

   @Override
   public String getName() {
      return this.field_411;
   }

   public void method_2(PacketSendEvent var1) {
   }

   public void method_2(NoFallModule var1) {
   }
}
