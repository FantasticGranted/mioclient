package me.mioclient.module.player;

import me.mioclient.api.BlockBreakingAccess;
import me.mioclient.event.AttackBlockEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.ExplosionDamage;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ItemSaverModule extends Module {
   public ItemSaverModule() {
      super("ItemSaver", "Prevents your tools from breaking.", Category.PLAYER);
   }

   @Subscribe
   public void method_2(AttackBlockEvent var1) {
      int var2 = mc.player.getInventory().selected;
      ItemStack var3 = mc.player.getInventory().getItem(var2);
      if (!method_101(var3)) {
         if (((BlockBreakingAccess)mc.gameMode).getCurrentBreakingBlock() != null && mc.options.keyAttack.isDown()) {
            InventoryUtil.method_16(var2 == 8 ? var2 - 1 : var2 + 1);
            var1.method_463();
         }
      }
   }

   public static boolean method_101(ItemStack var0) {
      return !var0.isEmpty() && var0.isDamageableItem() && !InventoryUtil.isArmor(var0) && !var0.is(Items.ELYTRA)
         ? ExplosionDamage.method_5(var0) > 10
         : true;
   }
}
