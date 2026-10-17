package me.mioclient.module.combat;

import java.util.ArrayList;
import java.util.List;
import me.mioclient.Hub;
import me.mioclient.enum_.Side3;
import me.mioclient.event.Event_12;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.MathUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.exploit.MultiTaskModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import me.mioclient.nick.Settings;

public class AutoClickerModule extends Module {
   public static final MultiTaskModule multitask = Hub.field_2595.method_78(MultiTaskModule.class);
   public Setting<Side3> mode;
   public Setting<Integer> cPS;
   public Setting<Float> delay;
   public Setting<Boolean> friendProtect;
   public final Timer field_1447;
   public float field_1448;
   public final List<Long> field_1449;

   public AutoClickerModule() {
      super("AutoClicker", "Spams attack as you hold down the attack button.", Category.COMBAT);
      Settings.initialize(this);
      this.field_1447 = new Timer();
      this.field_1448 = 1F;
      this.field_1449 = new ArrayList<>();
   }

   @Override
   public String getInfo() {
      return String.valueOf(this.field_1449.size());
   }

   @Subscribe
   public void method_2(Event_12 var1) {
      this.field_1449.removeIf(var0 -> System.currentTimeMillis() - var0 >= 1000L);
      long var2 = 1000L / (long)this.cPS.getValue().intValue();
      if (!this.delay.method_105()) {
         var2 = (long)((float)var2 * this.field_1448);
      }

      if (this.field_1447.method_9(var2)) {
         this.method_489();
         this.field_1449.add(System.currentTimeMillis());
         this.field_1447.reset();
         this.field_1448 = MathUtil.method_2(1F, this.delay.getValue());
      }
   }

   public boolean method_185(Entity var1) {
      if (this.isToggled() && this.friendProtect.getValue() && var1 instanceof Player var2 && Hub.field_2603.getId(var2)) {
         return true;
      }

      return false;
   }

   public void method_489() {
      if (!multitask.isToggled() || !mc.player.isUsingItem() && mc.screen == null) {
         KeyMapping.click(((me.mioclient.mixin.ducks.DuckKeyBinding)(Object)this.mode.getValue().method_688()).getKey());
      } else {
         this.mode.getValue().method_687();
      }
   }
}
