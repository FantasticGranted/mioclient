package me.mioclient.module.player;

import java.util.Set;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.ShulkerPreviewScreen;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import me.mioclient.nick.Settings;

public class InventoryCleanerModule extends Module {
   public Setting<Set<Item>> whitelist;
   public Setting<SelfFillMode> selection;
   public Setting<Integer> delay;
   public Setting<Integer> frequency;
   public Setting<Boolean> onlyUI;
   public Setting<Boolean> ignoreHotbar;
   public final Timer field_1279;

   public InventoryCleanerModule() {
      super("InventoryCleaner", "Gets rid of the selected items in your inventory.", Category.PLAYER);
      Settings.initialize(this);
      this.field_1279 = new Timer();
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_2(PlayerTickEvent var1) {
      int var2 = 0;
      if (this.field_1279.method_9((long)this.delay.getValue().intValue())) {
         Screen var3 = mc.screen;
         boolean var4 = var3 instanceof InventoryScreen
            || var3 instanceof ContainerScreen
            || var3 instanceof ShulkerBoxScreen && !(var3 instanceof ShulkerPreviewScreen);
         if (!this.onlyUI.getValue() || var4) {
            int var5 = mc.player.containerMenu.slots.size();
            if (mc.player.containerMenu instanceof InventoryMenu) {
               var5--;
            }

            for (Slot var7 : mc.player.containerMenu.slots) {
               ItemStack var8 = var7.getItem();
               if (!var8.isEmpty()
                  && this.selection.getValue().method_2(var8.getItem(), this.whitelist)
                  && (var7.index < var5 - 9 || !this.ignoreHotbar.getValue())) {
                  mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var7.index, 1, ContainerInput.THROW, mc.player);
                  this.field_1279.reset();
                  if (++var2 >= this.frequency.getValue()) {
                     break;
                  }
               }
            }
         }
      }
   }
}
