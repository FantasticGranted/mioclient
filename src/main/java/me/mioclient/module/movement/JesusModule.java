package me.mioclient.module.movement;

import me.mioclient.enum_.JesusMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.Event_35;
import me.mioclient.event.Subscribe;
import me.mioclient.mixin.ducks.DuckPlayerMoveC2SPacket;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import me.mioclient.nick.Settings;

public class JesusModule extends Module {
   public Setting<JesusMode> mode;
   public Setting<Boolean> strict;
   public Setting<Float> ascending;
   public final double[] field_1271;
   public int field_1272;

   public JesusModule() {
      super("Jesus", "Allows you to walk on water.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_1271 = new double[]{
         0.08232659236482401,
         0.13927999979019162,
         0.1654239999427795,
         0.11427136035293574,
         0.04194693730418576,
         0.01236341326161235
      };
   }

   @Override
   public void onToggle() {
      this.field_1272 = 0;
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.method_442()) {
         if (mc.player.isInWater()) {
            mc.player
               .push(
                  0.0,
                  this.mode.getValue() == JesusMode.DOLPHIN
                     ? 0.0399
                     : 0.08 * (double)this.ascending.getValue().floatValue(),
                  0.0
               );
         }
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (this.mode.getValue() == JesusMode.SOLID && this.method_442()) {
         if (var1.method_127() instanceof ServerboundMovePlayerPacket var2 && this.strict.getValue() && this.method_443()) {
            ((DuckPlayerMoveC2SPacket)(Object)var2).setOnGround(false);
            ((DuckPlayerMoveC2SPacket)(Object)var2).setY(var2.getY(0.0) + this.field_1271[this.field_1272++]);
            this.field_1272 = this.field_1272 % this.field_1271.length;
         }
      }
   }

   @Subscribe
   public void method_2(Event_35 var1) {
      if (!this.method_535() && !mc.player.isInWater() && this.mode.getValue() == JesusMode.SOLID && this.method_442()) {
         if (var1.method_958().is(Blocks.WATER)) {
            var1.method_9(
               Shapes.create(
                  new AABB(
                     0.0,
                     0.0,
                     0.0,
                     1.0,
                     0.98,
                     1.0
                  )
               )
            );
         }
      }
   }

   public boolean method_442() {
      return !this.method_535() && mc.player.input != null ? !mc.player.isPassenger() && !mc.player.input.keyPresses.shift() : false;
   }

   public boolean method_443() {
      return mc.level
         .getBlockState(BlockPos.containing(mc.player.position().subtract(0.0, 0.01, 0.0)))
         .is(Blocks.WATER);
   }
}
