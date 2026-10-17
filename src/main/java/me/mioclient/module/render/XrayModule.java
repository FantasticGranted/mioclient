package me.mioclient.module.render;

import java.util.Set;
import me.mioclient.api.ForceSettable;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import me.mioclient.nick.Settings;

public class XrayModule extends Module {
   public static XrayModule field_2913;
   public Setting<Boolean> bypass;
   public Setting<Set<Block>> whitelist;
   public double field_217;

   public XrayModule() {
      super("Xray", "Wallhack on ores.", Category.RENDER);
      Settings.initialize(this);
      field_2913 = this;
      this.whitelist.method_9(this::method_534);
      this.bypass.method_9(this::method_534);
   }

   @Override
   public void onEnable() {
      this.field_217 = (Double)mc.options.gamma().get();
      ((ForceSettable)(Object)mc.options.gamma()).forceSetValue(1e+03);
   }

   @Override
   public void onDisable() {
      ((ForceSettable)(Object)mc.options.gamma()).forceSetValue(this.field_217);
   }

   @Override
   public void onToggle() {
      this.method_534();
   }

   public boolean method_2(BlockPos var1, Block var2) {
      return var2 != null && var1 != null && !this.method_535()
         ? this.whitelist.getValue().contains(var2) && (!this.bypass.getValue() || this.method_391(var1))
         : false;
   }

   public boolean method_391(BlockPos var1) {
      for (Direction var5 : Direction.values()) {
         if (!mc.level.getBlockState(var1.relative(var5)).canOcclude()) {
            return true;
         }
      }

      return false;
   }

   public void method_534() {
      if (mc.level != null) {
         mc.levelRenderer.allChanged();
      }
   }

   public static XrayModule method_844() {
      return field_2913;
   }
}
