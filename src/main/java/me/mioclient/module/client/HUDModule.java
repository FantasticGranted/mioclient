package me.mioclient.module.client;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.HudFadeMode;
import me.mioclient.enum_.HudIconMode;
import me.mioclient.event.Event_26;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.AnimatedValue;
import me.mioclient.internal.HudElement;
import me.mioclient.internal.Class_0617;
import me.mioclient.internal.ClickGui;
import me.mioclient.internal.PlaceholderText;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.exploit.AntiLevitationModule;
import me.mioclient.module.render.NoRenderModule;
import me.mioclient.setting.BooleanSetting;
import me.mioclient.setting.ColorSetting;
import me.mioclient.setting.CustomSetting;
import me.mioclient.setting.CustomSetting3;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class HUDModule extends Module {
   public static AntiLevitationModule field_952 = Hub.field_2595.method_78(AntiLevitationModule.class);
   public static NoRenderModule norender = Hub.field_2595.method_78(NoRenderModule.class);
   public final Predicate<Boolean> field_953 = var0 -> mc.screen instanceof ClickGui var1x ? var1x.method_830() == null : true;
   public final Setting<Boolean> hudEditor = this.add(new BooleanSetting("HudEditor", false, this.field_953));
   public final Setting<HudIconMode> icons = this.add(new CustomSetting<>("Icons", HudIconMode.MOVE));
   public final Setting<Boolean> smoothWidth = this.add(new BooleanSetting("SmoothWidth", false));
   public final Setting<Integer> safeX = this.add(new CustomSetting3<>("SafeX", 1, 0, 20));
   public final Setting<Integer> safeY = this.add(new CustomSetting3<>("SafeY", 1, 0, 10));
   public final Setting<Boolean> colors = this.add(new BooleanSetting("Colors", true).method_220());
   public final Setting<Color> color = this.add(new ColorSetting("Color", new Color(189, 153, 255, 255)).method_8().method_2(this.colors));
   public final Setting<HudFadeMode> fade = this.add(new CustomSetting<>("Fade", HudFadeMode.NONE, var1x -> this.colors.method_194()));
   public final Setting<Integer> fadeOffset = this.add(
      new CustomSetting3<>(
         "FadeOffset",
         5,
         1,
         30,
         var1x -> (this.fade.getValue() != HudFadeMode.NONE || ((ColorSetting)this.color).method_132()) && this.colors.method_194()
      )
   );
   public final Setting<Color> secondColor = this.add(
      new ColorSetting("SecondColor", new Color(105, 86, 143, 255), var1x -> this.fade.getValue() == HudFadeMode.PULSE && this.colors.method_194())
         .method_8()
   );
   public final Setting<Integer> colorAmount = this.add(
      new CustomSetting3<>("ColorAmount", 5, 2, 9, var1x -> this.fade.getValue() == HudFadeMode.RAW && this.colors.method_194())
   );
   public final Setting<Integer> fadeSpeed = this.add(
      new CustomSetting3<>("FadeSpeed", 50, 20, 100, var1x -> this.fade.getValue() == HudFadeMode.RAW && this.colors.method_194())
   );
   public final Setting<Boolean> mixColors = this.add(
      new BooleanSetting("MixColors", true, var1x -> this.fade.getValue() == HudFadeMode.RAW && this.colors.method_194())
   );
   public final List<Setting<Color>> field_967 = new ArrayList<>(this.colorAmount.method_101());
   public final AnimatedValue field_968 = new AnimatedValue(2F);
   public final AnimatedValue field_969 = new AnimatedValue(1.5F);

   public HUDModule() {
      super("HUD", "Draws useful HUD elements on your screen.", Category.CLIENT);
      this.hudEditor.method_9(() -> {
         if (this.hudEditor.getValue()) {
            this.hudEditor.method_78(false);
            if (!this.method_535()) {
               mc.setScreen(Hub.method_756());
            }
         }
      });
      this.setDrawn(false);
      this.method_38(true);

      for (int var1 = 1; var1 <= this.colorAmount.method_101(); var1++) {
         int var2 = var1;
         Setting var3 = this.add(
            new ColorSetting(
                  new PlaceholderText().method_2(var1).method_9("Color\u0001"),
                  Color.white,
                  var2x -> this.fade.getValue() == HudFadeMode.RAW && this.colors.method_194() && var2 <= this.colorAmount.getValue()
               )
               .method_8()
         );
         this.field_967.add(var3);
      }
   }

   @Subscribe
   public void method_2(Event_26 var1) {
      int var2 = 0;
      if (this.icons.getValue() == HudIconMode.MOVE && !(mc.screen instanceof InventoryScreen)) {
         for (MobEffectInstance var4 : mc.player.getActiveEffects()) {
            Holder var5 = var4.getEffect();
            if ((!field_952.isToggled() || var5 != MobEffects.SLOW_FALLING && var5 != MobEffects.LEVITATION)
               && (!norender.isToggled() || !norender.darkness.getValue() || var5 != MobEffects.DARKNESS)) {
               if (var2 == 0) {
                  var2++;
               }

               if (!((MobEffect)var4.getEffect().value()).isBeneficial()) {
                  var2 = 2;
               }
            }
         }
      }

      if (mc.screen instanceof InventoryScreen) {
         var2 = 0;
      }

      this.field_968.method_3((float)(24 * var2 + 4));
      this.field_968.method_45();
      float var6 = this.field_969.method_45();
      float var7 = (float)mc.getWindow().getGuiScaledWidth();
      float var8 = (float)mc.getWindow().getGuiScaledHeight();
      if (var6 > 1F && !(mc.screen instanceof ChatScreen)) {
         var1.method_881()
            .fill(
               2,
               (int)(var8 - 2F - var6),
               (int)(var7 - 2F),
               (int)(var8 - 2F),
               mc.options.getBackgroundColor(Integer.MIN_VALUE)
            );
      }

      HudElement.field_460 = true;
      if (!(mc.screen instanceof Class_0617)) {
         Hub.method_756().method_2(var1.method_881(), 0.0, 0.0);
      }

      HudElement.field_460 = false;
   }

   public float method_338() {
      return this.field_969.current;
   }

   public float method_339() {
      return this.field_968.current == 4F ? 1F : this.field_968.current;
   }

   public void method_340(float var1) {
      this.field_969.method_3(var1);
   }

   public int method_341() {
      return this.safeX.getValue();
   }

   public int method_342() {
      return this.safeY.getValue();
   }

   public void method_334(int var1) {
      this.safeX.method_78(var1);
   }

   public void method_82(int var1) {
      this.safeY.method_78(var1);
   }
}
