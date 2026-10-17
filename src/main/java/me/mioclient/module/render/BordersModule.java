package me.mioclient.module.render;

import java.awt.Color;
import me.mioclient.enum_.SettingMode;
import me.mioclient.event.Event_3;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.SquareMarker;
import me.mioclient.setting.Setting;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import me.mioclient.nick.Settings;

public class BordersModule extends Module {
   public Setting<Color> chunk;
   public Setting<Color> map;
   public Setting<Float> lineWidth;
   public Setting<Integer> mapSize;
   public Setting<Integer> level;

   public BordersModule() {
      super("Borders", "Shows region borders on your screen.", Category.RENDER);
      Settings.initialize(this);
      this.level.method_2("~", SettingMode.MIN);
   }

   @Subscribe
   public void method_9(Event_3 var1) {
      if (this.chunk.getValue().getAlpha() != 0) {
         this.method_255().method_2(var1.method_10(), this.chunk.getValue(), this.level.getValue(), this.lineWidth.getValue());
      }

      if (this.map.getValue().getAlpha() != 0) {
         this.method_256().method_2(var1.method_10(), this.map.getValue(), this.level.getValue(), this.lineWidth.getValue());
      }
   }

   public SquareMarker method_255() {
      ChunkPos var1 = mc.player.chunkPosition();
      return new SquareMarker(var1.getMiddleBlockX(), var1.getMiddleBlockZ(), 16);
   }

   public SquareMarker method_256() {
      int var1 = 128 * (1 << this.mapSize.getValue());
      int var2 = Mth.floor(((double)mc.player.getBlockX() + 64.0) / (double)var1);
      int var3 = Mth.floor(((double)mc.player.getBlockZ() + 64.0) / (double)var1);
      int var4 = var2 * var1 + var1 / 2 - 64;
      int var5 = var3 * var1 + var1 / 2 - 64;
      return new SquareMarker(var4, var5, var1);
   }
}
