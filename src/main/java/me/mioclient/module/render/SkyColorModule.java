package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.enum_.SkyColorType;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import me.mioclient.nick.Settings;

public class SkyColorModule extends Module {
   public Setting<SkyColorType> type;
   public Setting<Boolean> dense;
   public Setting<Color> fog;
   public Setting<Color> sky;
   public Setting<Boolean> dimensions;
   public Setting<Boolean> overworld;
   public Setting<Boolean> nether;
   public Setting<Boolean> end;

   public SkyColorModule() {
      super("SkyColor", "Changes the fog color.", Category.RENDER);
      Settings.initialize(this);
      this.setDrawn(false);
      this.fog.method_5(var1 -> this.type.getValue() != SkyColorType.END);
      this.sky.method_5(var1 -> this.type.getValue() != SkyColorType.END);
      this.dense
         .method_5(
            var1 -> this.type.getValue() == SkyColorType.FLAT
                  || (this.nether.getValue() || this.end.getValue()) && this.type.getValue() == SkyColorType.NONE
         );
   }

   public boolean method_125() {
      return switch (BlockUtil.method_1071()) {
         case OVERWORLD -> this.overworld.getValue();
         case THE_NETHER -> this.nether.getValue();
         default -> this.end.getValue();
      };
   }
}
