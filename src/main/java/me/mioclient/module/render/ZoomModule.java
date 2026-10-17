package me.mioclient.module.render;

import me.mioclient.api.ForceSettable;
import me.mioclient.event.MouseScrollEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.TimedAnimation;
import me.mioclient.internal.Class_0095;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.util.Mth;
import me.mioclient.nick.Settings;

public class ZoomModule extends Module {
   public Setting<Boolean> smooth;
   public Setting<Boolean> smoothCamera;
   public Setting<Boolean> scroll;
   public Setting<Integer> amount;
   public final Class_0095 field_4309;
   public int field_3076;
   public boolean field_4310;
   public boolean field_1538;
   public final TimedAnimation field_4311;
   public final TimedAnimation field_4312;

   public ZoomModule() {
      super("Zoom", "Zooms your camera in.", Category.RENDER);
      Settings.initialize(this);
      this.field_4309 = new Class_0095(this);
      this.field_4311 = new TimedAnimation();
      this.field_4312 = new TimedAnimation();
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         this.field_4311.method_2(0.0F, (long)this.method_15(100));
         this.field_4312.method_2(0.0F, (long)this.method_15(100));
         mc.options.smoothCamera = this.field_4310;
      }
   }

   @Override
   public void onEnable() {
      if (this.method_535()) {
         this.disable();
      } else {
         lookups.method_14(this.field_4309);
         if (!this.field_1538) {
            this.field_3076 = (Integer)mc.options.fov().get();
         }

         this.field_4311.method_2(1F, (long)this.method_15(200));
         this.field_4310 = mc.options.smoothCamera;
      }
   }

   @Subscribe
   public void method_2(MouseScrollEvent var1) {
      if (var1.method_618() != 0.0 && this.scroll.getValue()) {
         int var2 = (int)((float)this.field_3076 - (float)Math.abs(75 / this.amount.getValue() - this.field_3076) * this.method_1176());
         int var3 = (int)Mth.clamp(
            (float)((double)this.field_4312.method_45() + var1.method_618() * 1e+01),
            (float)(var2 - this.field_3076),
            (float)(var2 - 10)
         );
         this.field_4312.method_2((float)var3, (long)this.method_15(100));
         var1.method_463();
      }
   }

   public void method_231(int var1) {
      ((ForceSettable)(Object)mc.options.fov()).forceSetValue(var1);
   }

   public int method_1175() {
      int var1 = (int)((float)this.field_3076 - (float)Math.abs(75 / this.amount.getValue() - this.field_3076) * this.method_1176());
      return this.scroll.getValue()
         ? (int)Mth.clamp((float)var1 - this.field_4312.method_45(), 1e+01F, 1.5e+02F)
         : var1;
   }

   public float method_1176() {
      return (float)(
         1.0
            - Math.pow((double)(1F - this.field_4311.method_45()), 4.0)
      );
   }

   public int method_15(int var1) {
      return !this.smooth.getValue() ? 0 : var1;
   }
}
