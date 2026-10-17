package me.mioclient.module.movement;

import me.mioclient.Hub;
import me.mioclient.enum_.WarpRechargeMode;
import me.mioclient.enum_.WarpMode;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_1359;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import me.mioclient.nick.Settings;

public class WarpModule extends Module {
   public static FakeLagModule field_1107 = Hub.field_2595.method_78(FakeLagModule.class);
   public Setting<WarpMode> mode;
   public Setting<Integer> boost;
   public Setting<Integer> charge;
   public Setting<Integer> chargeSpeed;
   public Setting<WarpRechargeMode> recharge;
   public Setting<Boolean> fakeLag;
   public Setting<Boolean> disableGround;
   public Setting<Boolean> limitPackets;
   public Setting<Boolean> autoDisable;
   public final Class_1359 field_1117;
   public boolean field_1118;
   public volatile int field_1119;

   public WarpModule() {
      super("Warp", "Allows you to dash forward after standing still for a certain amount of time.", Category.MOVEMENT, "tickshift");
      Settings.initialize(this);
      this.field_1117 = new Class_1359(this);
   }

   @Override
   public String getInfo() {
      return this.mode.getValue() == WarpMode.ALTERNATIVE ? String.valueOf(this.field_1117.field_4427.size()) : String.valueOf(this.field_1119);
   }

   @Override
   public void onEnable() {
      if (this.recharge.getValue() == WarpRechargeMode.INSTANT) {
         this.field_1119 = this.charge.getValue();
      }
   }

   @Override
   public void onDisable() {
      this.method_105(false);
      if (this.mode.getValue() == WarpMode.ALTERNATIVE && !this.method_535()) {
         try {
            while (!this.field_1117.field_4427.isEmpty()) {
               mc.player.connection.send(this.field_1117.field_4427.poll());
            }
         } catch (Exception var2) {
         }

         this.field_1117.field_4427.clear();
      }
   }

   @Subscribe(
      method_800 = -200
   )
   public void method_7(MoveEvent var1) {
      if (this.mode.getValue() == WarpMode.PLAIN) {
         if (!this.method_385()) {
            Hub.field_2617.method_38(this);
            this.method_105(false);
            this.field_1118 = false;
         } else if (this.field_1119 == 0 || this.field_1118) {
            Hub.field_2617.method_38(this);
            if (this.autoDisable.getValue()) {
               this.method_68();
            }

            this.method_105(false);
         } else if (this.field_1119 > 0) {
            if (this.fakeLag.getValue()) {
               boolean var2 = field_1107.isToggled();
               this.method_105(mc.player.onGround() || !this.disableGround.getValue());
               if (this.boost.getValue() > 1 && this.limitPackets.getValue()) {
                  Hub.field_2602.method_985().reset();
               }

               if (!var2) {
                  return;
               }
            }

            Hub.field_2617.method_2(this, (float)this.boost.getValue().intValue());
         }
      }
   }

   @Subscribe
   public void method_9(Event_19 var1) {
      if (this.mode.getValue() == WarpMode.ALTERNATIVE) {
         if (var1.method_213() == PreType.PRE) {
            if (!this.field_1117.field_4427.isEmpty() && this.method_385()) {
               Hub.field_2617.method_2(this, (float)this.boost.getValue().intValue());
               mc.player.connection.send(this.field_1117.field_4427.poll());
            } else if (!this.field_1117.field_4427.isEmpty() && !this.field_1118) {
               Hub.field_2617.method_38(this);
            } else {
               Hub.field_2617.method_38(this);
               if (this.autoDisable.getValue()) {
                  this.method_68();
               }
            }
         }
      }
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_1118 = true;
      }
   }

   public void method_105(boolean var1) {
      if (this.fakeLag.getValue() && mc.player != null) {
         field_1107.method_38(var1);
      }
   }

   public boolean method_385() {
      return mc.player.input.keyPresses.right()
         || mc.player.input.keyPresses.left()
         || mc.player.input.keyPresses.backward()
         || mc.player.input.keyPresses.forward()
         || mc.player.getX() - mc.player.xo != 0.0
         || mc.player.getY() - mc.player.yo != 0.0
         || mc.player.getZ() - mc.player.zo != 0.0;
   }

   public int method_165() {
      return this.mode.getValue() == WarpMode.ALTERNATIVE ? this.field_1117.field_4427.size() : this.field_1119;
   }

   public boolean method_363() {
      return mc.player.position().distanceToSqr(mc.player.xo, mc.player.yo, mc.player.zo) > 0.0;
   }
}
