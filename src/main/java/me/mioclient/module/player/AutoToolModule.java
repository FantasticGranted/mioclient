package me.mioclient.module.player;

import me.mioclient.api.BlockBreakingAccess;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.world.level.block.state.BlockState;
import me.mioclient.nick.Settings;

public class AutoToolModule extends Module {
   public Setting<Boolean> swapBack;
   public int field_1071;
   public boolean field_1101;

   public AutoToolModule() {
      super("AutoTool", "Equips the best tool to use on a block for you.", Category.PLAYER);
      Settings.initialize(this);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (((BlockBreakingAccess)mc.gameMode).getCurrentBreakingBlock() != null && mc.options.keyAttack.isDown()) {
         this.field_1101 = true;
         int var2 = mc.player.getInventory().selected;
         BlockState var3 = mc.level.getBlockState(((BlockBreakingAccess)mc.gameMode).getCurrentBreakingBlock());
         double var4 = (double)mc.player.getInventory().getItem(var2).getDestroySpeed(var3);

         for (int var6 = 0; var6 < 9; var6++) {
            double var7 = (double)mc.player.getInventory().getItem(var6).getDestroySpeed(var3);
            if (var7 > var4) {
               var4 = var7;
               var2 = var6;
            }
         }

         InventoryUtil.method_16(var2);
      } else if (this.swapBack.getValue()) {
         if (this.field_1101) {
            mc.player.getInventory().selected = this.field_1071;
            this.field_1101 = false;
         } else {
            this.field_1071 = mc.player.getInventory().selected;
         }
      }
   }
}
