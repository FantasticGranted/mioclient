package me.mioclient.module.player;

import me.mioclient.enum_.UpgradeItemFilter;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import me.mioclient.nick.Settings;

public class AutoUpgradeModule extends Module {
   public static final Item field_3322 = Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE;
   public static final int field_3323 = 0;
   public static final int field_3324 = 1;
   public static final int field_3325 = 2;
   public static final int field_3326 = 3;
   public Setting<UpgradeItemFilter> items;
   public Setting<Integer> delay;
   public Setting<Boolean> safe;
   public final Timer field_3330;

   public AutoUpgradeModule() {
      super("AutoUpgrade", "Automatically upgrades diamond items to netherite.", Category.PLAYER);
      Settings.initialize(this);
      this.field_3330 = new Timer();
   }

   @Subscribe
   public void method_9(PostTickEvent var1) {
      AbstractContainerMenu var2 = mc.player.containerMenu;
      if (mc.screen instanceof SmithingScreen) {
         if (this.field_3330.method_9((long)this.delay.getValue().intValue())) {
            if (this.method_2(var2, field_3322) > 1 || !this.safe.getValue()) {
               if (!var2.getSlot(3).getItem().isEmpty()) {
                  mc.gameMode.handleContainerInput(var2.containerId, 3, 0, ContainerInput.QUICK_MOVE, mc.player);
                  this.field_3330.reset();
               } else {
                  ItemStack var3 = var2.getSlot(0).getItem();
                  if (var3.isEmpty() || var3.is(field_3322) && var3.getCount() == 1) {
                     for (int var6 = 4; var6 < 40; var6++) {
                        if (var2.getSlot(var6).getItem().is(field_3322)) {
                           InventoryUtil.method_5(var6, 0);
                           this.field_3330.reset();
                           break;
                        }
                     }
                  } else if (var2.getSlot(1).getItem().isEmpty()) {
                     int var5 = this.method_2(var2);
                     if (var5 != -1) {
                        InventoryUtil.method_5(var5, 1);
                        this.field_3330.reset();
                     }
                  } else {
                     if (var2.getSlot(2).getItem().isEmpty()) {
                        for (int var4 = 4; var4 < 40; var4++) {
                           if (var2.getSlot(var4).getItem().is(Items.NETHERITE_INGOT)) {
                              InventoryUtil.method_5(var4, 2);
                              this.field_3330.reset();
                              break;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   public int method_2(AbstractContainerMenu var1, Item var2) {
      int var3 = 0;

      for (Slot var5 : var1.slots) {
         if (var5.getItem().is(var2)) {
            var3 += var5.getItem().getCount();
         }
      }

      return var3;
   }

   public int method_2(AbstractContainerMenu var1) {
      boolean var2 = this.items.getValue() == UpgradeItemFilter.TOOLS;
      boolean var3 = this.items.getValue() == UpgradeItemFilter.ARMOR;

      for (int var4 = 4; var4 < 40; var4++) {
         ItemStack var5 = var1.getSlot(var4).getItem();
         net.minecraft.world.item.enchantment.Repairable var6 = var5.get(net.minecraft.core.component.DataComponents.REPAIRABLE);
         if (!var2
            && InventoryUtil.isArmor(var5)
            && var6 != null
            && this.method_2(net.minecraft.world.item.crafting.Ingredient.of(var6.items()))) {
            return var4;
         }

         net.minecraft.world.item.enchantment.Repairable var8 = var5.get(net.minecraft.core.component.DataComponents.REPAIRABLE);
         if (!var3 && var8 != null && !InventoryUtil.isArmor(var5) && !var5.is(Items.ELYTRA) && this.method_2(net.minecraft.world.item.crafting.Ingredient.of(var8.items()))) {
            return var4;
         }
      }

      return -1;
   }

   public boolean method_2(Ingredient var1) {
      for (ItemStack var5 : var1.items().map(net.minecraft.world.item.ItemStack::new).toList()) {
         if (var5.is(Items.DIAMOND)) {
            return true;
         }
      }

      return false;
   }
}
