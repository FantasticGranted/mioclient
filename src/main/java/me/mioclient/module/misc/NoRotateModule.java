package me.mioclient.module.misc;

import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.RotationManager;
import me.mioclient.mixin.ducks.DuckPlayerPositionLookS2CPacket;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.entity.PositionMoveRotation;
import net.minecraft.world.entity.Relative;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;
import me.mioclient.nick.Settings;

public class NoRotateModule extends Module {
   public Setting<Boolean> field_2787;
   public Setting<Boolean> stopInWebs;

   public NoRotateModule() {
      super("NoRotate", "Cancels the rotations server sets for you.", Category.MISC);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (this.field_2787.getValue()
         && !MovementUtil.method_363()
         && !mc.player.isSuppressingSlidingDownLadder()
         && var1.method_127() instanceof ServerboundMovePlayerPacket
         && (!this.stopInWebs.getValue() || !PlayerUtil.method_5(mc.player))) {
         BlockState var2 = mc.level.getBlockState(mc.player.blockPosition());
         if (!(var2.getBlock() instanceof FallingBlock)) {
            VoxelShape var3 = var2.getCollisionShape(mc.level, BlockPos.ZERO);
            if (!var3.isEmpty() && !(mc.player.getY() - Math.floor(mc.player.getY()) >= var3.bounds().maxY)) {
               AABB var4 = var3.bounds().move(mc.player.blockPosition());
               if (var4.intersects(mc.player.getBoundingBox().inflate(-0.05))) {
                  var1.method_463();
               }
            }
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (!mc.isLocalServer() && !mc.player.isSuppressingSlidingDownLadder()) {
         if (!RotationManager.method_513() || mc.player.tickCount > 20) {
            if (var1.method_127() instanceof ClientboundPlayerPositionPacket var2 && !this.method_535() && mc.player.tickCount > 5) {
               float var5 = mc.player.getYRot();
               float var4 = mc.player.getXRot();
               if (var2.relatives().contains(Relative.X_ROT)) {
                  var5 = 0.0F;
               }

               if (var2.relatives().contains(Relative.Y_ROT)) {
                  var4 = 0.0F;
               }

                PositionMoveRotation var6 = var2.change();
                ((DuckPlayerPositionLookS2CPacket)(Object)var2).setChange(new PositionMoveRotation(var6.position(), var6.deltaMovement(), var5, var4));
            }
         }
      }
   }
}
