package me.mioclient.module.render;

import java.awt.Color;
import java.util.Calendar;
import me.mioclient.api.ForceSettable;
import me.mioclient.enum_.FullbrightMode;
import me.mioclient.enum_.Weather;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import me.mioclient.nick.Settings;

public class AmbienceModule extends Module {
   public Setting<FullbrightMode> brightness;
   public Setting<Color> color;
   public Setting<Integer> lightLevel;
   public Setting<Boolean> worldTime;
   public Setting<Boolean> sync;
   public Setting<Float> time;
   public Setting<Boolean> worldWeather;
   public Setting<Weather> weather;
   public Setting<Boolean> force;
   public Setting<Float> amount;
   public double field_217;

   public AmbienceModule() {
      super("Ambience", "Changes several ambient things.", Category.RENDER);
      Settings.initialize(this);
      this.lightLevel.method_9(() -> {
         if (this.brightness.getValue() == FullbrightMode.SKY && mc.level != null) {
            mc.levelRenderer.allChanged();
         }
      });
      this.brightness
         .method_9(
            () -> {
               if (!this.method_535()) {
                  if (mc.player.hasEffect(MobEffects.NIGHT_VISION)
                     && mc.player.getEffect(MobEffects.NIGHT_VISION).getAmplifier() == 68) {
                     mc.player.removeEffect(MobEffects.NIGHT_VISION);
                  }

                  if (this.brightness.getValue() != FullbrightMode.GAMMA) {
                     ((ForceSettable)(Object)mc.options.gamma()).forceSetValue(this.field_217);
                  }
               }
            }
         );
      this.setDrawn(false);
   }

   @Override
   public void onEnable() {
      this.field_217 = (Double)mc.options.gamma().get();
   }

   @Override
   public void onDisable() {
      if (!this.method_535()) {
         mc.player.removeEffect(MobEffects.NIGHT_VISION);
      }

      ((ForceSettable)(Object)mc.options.gamma()).forceSetValue(this.field_217);
   }

   @Override
   public void onToggle() {
      if (mc.levelRenderer != null) {
         if (this.brightness.getValue() == FullbrightMode.SKY || this.brightness.getValue() == FullbrightMode.SCREEN) {
            mc.levelRenderer.allChanged();
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         if (this.worldTime.getValue()) {
            long var2 = this.method_94();
            mc.level.getLevelData().setGameTime(var2);
            // mc.level.setDayTime(var2); // TODO(runtime): day-time setter removed in 26.1
         }

         if (this.brightness.getValue() == FullbrightMode.GAMMA) {
            ((ForceSettable)(Object)mc.options.gamma()).forceSetValue(1e+03);
         } else if (this.brightness.getValue() == FullbrightMode.POTION && !mc.player.hasEffect(MobEffects.NIGHT_VISION)) {
            mc.player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, -1, 68));
         }
      }
   }

   public long method_94() {
      if (this.sync.getValue()) {
         Calendar var1 = Calendar.getInstance();
         return (long)(
            ((float)var1.get(11) + (float)var1.get(12) / 6e+01F) * 1e+03F
               + 1.8e+04F
         );
      } else {
         return (long)(this.time.getValue() * 1e+03F + 1.8e+04F);
      }
   }

   public boolean method_95() {
      return this.isToggled() && this.worldWeather.getValue() && this.force.getValue();
   }
}
