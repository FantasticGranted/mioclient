package me.mioclient.module.player;

import me.mioclient.Hub;
import me.mioclient.enum_.PreType;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_19;
import me.mioclient.event.TravelEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Class_0245;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.render.FreeLookModule;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class RotationLockModule extends Module {
   public static FreeLookModule freelook = Hub.field_2595.method_78(FreeLookModule.class);
   public Setting<Boolean> yaw;
   public Setting<Boolean> custom;
   public Setting<Float> value;
   public Setting<Boolean> pitch;
   public Setting<Float> value2;

   public RotationLockModule() {
      super("RotationLock", "Locks your rotation.", Category.PLAYER);
      Settings.initialize(this);
      this.value.method_31("YawValue");
      this.value2.method_31("PitchValue");
   }

   @Subscribe(
      method_800 = 200
   )
   public void method_9(Event_19 var1) {
      if (var1.method_213() == PreType.PRE && freelook.isToggled()) {
         if (this.yaw.getValue()) {
            var1.setYaw(this.method_500());
         }

         if (this.pitch.getValue()) {
            var1.setPitch(this.method_501());
         }
      }
   }

   @Subscribe(
      method_800 = 200
   )
   public void method_9(TravelEvent var1) {
      if (freelook.isToggled()) {
         if (this.yaw.getValue()) {
            var1.setYaw(this.method_500());
            var1.method_463();
         }

         if (this.pitch.getValue()) {
            var1.setPitch(this.method_501());
            var1.method_463();
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.yaw.getValue()) {
         mc.player.setYRot(this.method_500());
      }

      if (this.pitch.getValue()) {
         mc.player.setXRot(this.method_501());
      }
   }

   public float method_500() {
      return !this.custom.getValue()
         ? (float)(Math.round((mc.player.getYRot() + 1F) / (float)Class_0245.field_684) * Class_0245.field_684)
         : this.value.getValue();
   }

   public float method_501() {
      return this.value2.getValue();
   }
}
