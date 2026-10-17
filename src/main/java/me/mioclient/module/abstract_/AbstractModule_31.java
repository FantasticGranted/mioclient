package me.mioclient.module.abstract_;

import me.mioclient.Hub;
import me.mioclient.enum_.SettingMode;
import me.mioclient.internal.BoxUtil;
import me.mioclient.module.Category;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class AbstractModule_31 extends AbstractModule_8 {
   public Object field_351;

   public AbstractModule_31() {
      super("FishTrap", "Traps your enemies in fish (crawling) position.", Category.COMBAT);
      this.method_139();
      this.method_5(false);
      this.unregister(this.targets);
      this.unregister(this.head);
      this.unregister(this.face);
      this.unregister(this.legs);
      this.head.method_78(true);
      this.face.method_78(false);
      this.legs.method_78(false);
   }

   @Override
   public Vec3 method_14(Player var1) {
      if (!this.method_140().method_105()) {
         AABB var2 = Hub.field_2612.method_2(var1, this.method_140().getValue());
         return BoxUtil.method_2(var2);
      } else {
         return super.method_14(var1);
      }
   }

   @Override
   public boolean method_36(Player var1) {
      return var1.getBoundingBox().getYsize() > 1.0 ? false : super.method_36(var1);
   }

   public void method_139() {
      Setting var1 = this.add(new CustomSetting3("Extrapolation", 0, 0, 6));
      var1.method_2("None", SettingMode.MIN);
      this.field_351 = var1;
   }

   public Setting<Integer> method_140() {
      return (Setting<Integer>)this.field_351;
   }
}
