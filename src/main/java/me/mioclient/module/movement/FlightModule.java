package me.mioclient.module.movement;

import me.mioclient.enum_.AntiKickMode;
import me.mioclient.enum_.FlightMode;
import me.mioclient.event.Event_19;
import me.mioclient.event.PacketReceiveEvent;
import me.mioclient.event.MoveEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.MovementUtil;
import me.mioclient.internal.FontRenderer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.network.protocol.game.ClientboundPlayerPositionPacket;
import me.mioclient.nick.Settings;

public class FlightModule extends Module {
   public Setting<FlightMode> mode;
   public Setting<Float> speed;
   public Setting<Boolean> vertical;
   public Setting<Float> vSpeed;
   public Setting<Float> glide;
   public Setting<AntiKickMode> antiKick;
   public Setting<Boolean> accelerate;
   public Setting<Float> accelMin;
   public Setting<Float> accelTime;
   public long field_303;

   public FlightModule() {
      super("Flight", "Allows you to fly using magic.", Category.MOVEMENT);
      Settings.initialize(this);
      this.speed.method_214();
      this.vSpeed.method_214();
      this.mode.method_9(() -> {
         if (this.isToggled()) {
            mc.player.getAbilities().flying = false;
         }
      });
   }

   @Override
   public String getInfo() {
      try {
         return FontRenderer.method_3(this.mode.getValue());
      } catch (Exception var2) {
         return null;
      }
   }

   @Override
   public void onEnable() {
      this.field_303 = System.currentTimeMillis();
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         if (this.mode.getValue() == FlightMode.VANILLA) {
            mc.player.getAbilities().flying = false;
         }
      }
   }

   @Subscribe
   public void method_2(Event_19 var1) {
      this.mode.getValue().method_2(this, var1);
      this.antiKick.getValue().method_2(var1);
   }

   @Subscribe
   public void method_2(MoveEvent var1) {
      if (!MovementUtil.method_363()) {
         this.field_303 = System.currentTimeMillis();
      }

      this.mode.getValue().method_2(this, var1);
      this.antiKick.getValue().method_2(var1);
   }

   @Subscribe
   public void method_5(PacketReceiveEvent var1) {
      if (var1.method_127() instanceof ClientboundPlayerPositionPacket) {
         this.field_303 = System.currentTimeMillis();
      }
   }

   public double method_874() {
      return this.accelerate.getValue()
         ? MovementUtil.method_2(
            (double)this.speed.getValue().floatValue(),
            (double)this.accelMin.getValue().floatValue(),
            (double)this.accelTime.getValue().floatValue(),
            this.field_303
         )
         : (double)this.speed.getValue().floatValue();
   }
}
