package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.FastWebMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.SprintEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.PlayerUtil;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.abstract_.AbstractModule_28;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket.Action;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class FastWebModule extends Module {
   public static final AbstractModule_28 field_2449 = Hub.field_2595.method_78(AbstractModule_28.class);
   public Setting<FastWebMode> field_2450;
   public Setting<Boolean> horizontal;
   public Setting<Float> speed;
   public Setting<Boolean> vertical;
   public Setting<Float> vSpeed;
   public Setting<Boolean> upwards;
   public boolean field_2456;

   public FastWebModule() {
      super("FastWeb", "Makes you faster in cobwebs.", Category.MOVEMENT);
      Settings.initialize(this);
      this.field_2456 = false;
      this.setDrawn(false);
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (var1.method_213() == PreType.PRE) {
         Vec3 var2 = new Vec3(
            mc.player.getX() - mc.player.xo,
            mc.player.getY() - mc.player.yo,
            mc.player.getZ() - mc.player.zo
         );
         if (this.method_726() && var2.lengthSqr() > 0.0) {
            BlockPos.betweenClosedStream(mc.player.getBoundingBox())
               .<BlockPos>map(BlockPos::immutable)
               .filter(var0 -> mc.level.getBlockState(var0).is(Blocks.COBWEB))
               .forEach(var0 -> {
                  if (!field_2449.method_1052()) {
                     PacketSender.method_2(Action.ABORT_DESTROY_BLOCK, var0, Direction.UP);
                  }

                  PacketSender.method_2(Action.STOP_DESTROY_BLOCK, var0, Direction.UP);
               });
         }

         if (this.field_2450.getValue() == FastWebMode.GRIM
            && mc.player.input.keyPresses.shift()
            && !mc.player.onGround()
            && PlayerUtil.method_5(mc.player)) {
            var1.setY(mc.player.getY() + Math.random() + 1.0);
         }
      }
   }

   @Subscribe(
      method_800 = -100
   )
   public void method_2(SprintEvent var1) {
      if (this.method_726() && !mc.player.onGround() && mc.player.input.keyPresses.jump() && PlayerUtil.method_5(mc.player)) {
         mc.player.setSprinting(false);
      }
   }

   @Subscribe
   public void method_2(MoveEvent var1) {
      if (Hub.field_2602.method_984().method_9(500L) && PlayerUtil.method_5(mc.player) && this.field_2450.getValue() != FastWebMode.GRIM) {
         if (this.vertical.getValue()) {
            if (!mc.player.input.keyPresses.shift() || mc.player.onGround()) {
               this.reset();
            } else if (this.method_724()) {
               this.field_2456 = true;
               Hub.field_2617.method_2(this, this.vSpeed.getValue() * 6F);
            } else {
               this.reset();
               mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, (double)(-this.vSpeed.getValue())));
               var1.setY((double)(-this.vSpeed.getValue()));
            }

            if (this.upwards.getValue() && mc.player.input.keyPresses.jump() && !this.method_724()) {
               mc.player.setDeltaMovement(mc.player.getDeltaMovement().with(Axis.Y, (double)this.vSpeed.getValue().floatValue()));
               var1.setY((double)this.vSpeed.getValue().floatValue());
            }
         }

         if (MovementUtil.method_363() && this.horizontal.getValue()) {
            double[] var2 = MovementUtil.method_2(var1, MovementUtil.method_78(true) * (double)this.speed.getValue().floatValue());
            mc.player.setDeltaMovement(var2[0], mc.player.getDeltaMovement().y, var2[1]);
         }
      } else {
         this.reset();
      }
   }

   public boolean method_724() {
      return this.field_2450.getValue() == FastWebMode.STRICT;
   }

   public boolean method_725() {
      return this.field_2456;
   }

   public void reset() {
      Hub.field_2617.method_38(this);
      this.field_2456 = false;
   }

   public boolean method_726() {
      return this.isToggled() && this.field_2450.getValue() == FastWebMode.GRIM;
   }
}
