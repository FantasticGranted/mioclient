package me.mioclient.module.misc;

import java.awt.Color;
import java.util.Comparator;
import me.mioclient.Hub;
import me.mioclient.enum_.RelationType;
import me.mioclient.enum_.TabLatencyMode;
import me.mioclient.enum_.TabSort;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.Optionull;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;
import me.mioclient.nick.Settings;

public class ExtraTabModule extends Module {
   public Setting<TabLatencyMode> latency;
   public Setting<Float> scale;
   public Setting<Boolean> betterSkin;
   public Setting<Boolean> highlight;
   public Setting<Color> self;
   public Setting<Boolean> social;
   public Setting<Boolean> sort;
   public Setting<TabSort> mode;
   public Setting<Boolean> reverse;

   public ExtraTabModule() {
      super("ExtraTab", "Allows you to customize your TAB-screen.", Category.MISC);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   public float method_54() {
      return !this.isToggled() ? 1F : this.scale.getValue();
   }

   public int method_2(PlayerInfo var1) {
      return switch ((TabSort)this.mode.getValue()) {
         case ALPHABET -> 0;
         case SOCIALS -> {
            RelationType var2 = Hub.field_2603.method_253(var1.getProfile().name());
            byte var3 = 0;
            if (var2 == RelationType.FRIEND) {
               var3 = -1;
            }

            if (var2 == RelationType.ENEMY) {
               var3 = -2;
            }

            yield var3;
         }
         case LATENCY -> -var1.getLatency();
         case LENGTH -> -mc.font.width(var1.getProfile().name());
      };
   }

   public Comparator<PlayerInfo> method_55() {
      Comparator var1 = Comparator.<PlayerInfo>comparingInt(var1x -> {
            int var2 = var1x.getGameMode() == GameType.SPECTATOR ? 1 : 0;
            if (this.reverse.getValue()) {
               var2 *= -1;
            }

            return var2;
         })
         .thenComparing(var0 -> (String)Optionull.mapOrDefault(var0.getTeam(), PlayerTeam::getName, ""))
         .thenComparing(var1x -> this.method_2(var1x))
         .thenComparing(var0 -> var0.getProfile().name(), String::compareToIgnoreCase);
      if (this.reverse.getValue()) {
         var1 = var1.reversed();
      }

      return var1;
   }
}
