package me.mioclient.module.abstract_;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import me.mioclient.enum_.ScreenCorner;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.HudTextElement;
import me.mioclient.internal.RegistrySetting;
import me.mioclient.internal.TextHudElement;
import me.mioclient.internal.Class_1162;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.Setting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;

public class AbstractModule_35 extends AbstractModule_26 {
   public final Setting<Boolean> vanilla = this.add(new BooleanSetting("Vanilla", false));
   public final Setting<SelfFillMode> selection = this.add(new CustomSetting<>("Selection", SelfFillMode.ANY));
   public final Setting<Set<MobEffect>> field_4298 = this.add(new RegistrySetting<>("WhiteList", BuiltInRegistries.MOB_EFFECT));
   public final List<Class_1162> field_4299 = new ArrayList<>();

   public AbstractModule_35() {
      super("Effects", "potions");
      TextHudElement var1 = new TextHudElement(this, this.field_4299) {
         @Override
         public Color method_2(float var1, HudTextElement var2) {
            MobEffect var3 = (MobEffect)((Class_1162)var2).method_1026().value();
            return AbstractModule_35.this.vanilla.getValue() ? new Color(var3.getColor(), false) : super.method_2(var1, var2);
         }
      };
      this.method_2(var1);
      this.method_14().method_2(ScreenCorner.BOTTOM_RIGHT);
   }

   @Override
   public void onEnable() {
      this.field_4299.clear();
      BuiltInRegistries.MOB_EFFECT
         .stream()
         .map(BuiltInRegistries.MOB_EFFECT::wrapAsHolder)
         .forEach(
            var1 -> this.field_4299
                  .add(
                     new Class_1162(
                        var1, () -> mc.player.hasEffect(var1) && this.selection.getValue().method_2((MobEffect)var1.value(), this.field_4298)
                     )
                  )
         );
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      this.field_4299.sort(Comparator.comparing(var0 -> ((MobEffect)var0.method_1026().value()).getDisplayName().getString()));
   }
}
