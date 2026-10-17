package me.mioclient.module.player;

import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.MovementUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.combat.AutoCrystalModule;
import me.mioclient.module.exploit.XCarryModule;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class ReplenishModule extends Module {
   public static XCarryModule field_1340 = Hub.field_2595.method_78(XCarryModule.class);
   public static AutoCrystalModule autoCrystal = Hub.field_2595.method_78(AutoCrystalModule.class);
   public Setting<Set<Item>> items;
   public Setting<SelfFillMode> selection;
   public Setting<Integer> delay;
   public Setting<Integer> threshold;
   public Setting<Boolean> sort;
   public Setting<Boolean> unstackable;
   public Setting<Boolean> limit;
   public Setting<Boolean> motion;
   public Setting<Boolean> falling;
   public final List<Item> field_1350;
   public final Timer field_1351;

   public ReplenishModule() {
      super("Replenish", "Replenishes your hotbar.", Category.PLAYER);
      Settings.initialize(this);
      this.field_1350 = NonNullList.withSize(9, Items.AIR);
      this.field_1351 = new Timer();
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (this.field_1351.method_9((long)this.delay.getValue().intValue()) && mc.player.containerMenu.getCarried().isEmpty()) {
         if ((!MovementUtil.method_363() || !this.motion.getValue()) && (!(mc.player.getDeltaMovement().y() > 0.0) || !this.falling.getValue())) {
            boolean var2 = mc.screen instanceof AbstractContainerScreen;

            for (int var3 = 0; var3 < 9; var3++) {
               ItemStack var4 = mc.player.getInventory().getItem(var3);
               Item var5 = this.field_1350.get(var3);
               if (this.unstackable.getValue() && var4.isEmpty() && var5 != Items.AIR && !var2 && this.method_2(var5, var3)) {
                  this.field_1351.reset();
                  return;
               }

               this.field_1350.set(var3, var4.getItem());
            }

            if (!var2 || mc.screen instanceof InventoryScreen) {
               for (int var6 = 0; var6 < 9; var6++) {
                  if (this.method_465(var6)) {
                     this.field_1351.reset();
                     return;
                  }
               }
            }
         }
      }
   }

   public boolean method_2(Item var1, int var2) {
      if (!this.selection.getValue().method_2(var1, this.items.getValue())) {
         return false;
      } else {
         boolean var3 = autoCrystal.isToggled() && autoCrystal.forceSuicide.getValue();
         int var4 = InventoryUtil.method_2(
            var1, var1x -> var1x != mc.player.getOffhandItem() && (!var3 || var1x.getItem() != Items.TOTEM_OF_UNDYING), true
         );
         if (var4 == -1 && var1 == Items.TOTEM_OF_UNDYING && !var3) {
            var4 = InventoryUtil.method_2((Predicate<ItemStack>)(var1x -> this.method_35(var1x.getItem()) && var1x != mc.player.getOffhandItem()), true);
         }

         if (var4 < 36 && var4 != -1) {
            if (this.method_113(var2)) {
               mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var4, 0, ContainerInput.QUICK_MOVE, mc.player);
            } else {
               InventoryUtil.method_5(var4, InventoryUtil.getId(var2));
            }

            return true;
         } else {
            return false;
         }
      }
   }

   public boolean method_113(int var1) {
      for (int var2 = 0; var2 < 9; var2++) {
         if (var2 != var1) {
            ItemStack var3 = mc.player.getInventory().getItem(var2);
            if (var3.isEmpty()) {
               return false;
            }
         }
      }

      return true;
   }

   public boolean method_35(Item var1) {
      return var1 == Items.ENCHANTED_GOLDEN_APPLE || var1 == Items.GOLDEN_APPLE;
   }

   public boolean method_465(int var1) {
      ItemStack var2 = mc.player.getInventory().getItem(var1);
      if (!this.selection.getValue().method_2(var2.getItem(), this.items.getValue())) {
         return false;
      } else {
         float var3 = (float)this.threshold.getValue().intValue() / 64F;
         if (!var2.isEmpty() && var2.getItem() != Items.AIR && var2.isStackable() && !((float)var2.getCount() / (float)var2.getMaxStackSize() >= var3)) {
            int var4 = -1;
            int var5 = 0;

            for (int var6 = 9; var6 < 36; var6++) {
               ItemStack var7 = mc.player.getInventory().getItem(var6);
               boolean var8 = ItemStack.isSameItemSameComponents(var7, var2);
               if (!var7.isEmpty() && var8 && var7.getCount() > var5) {
                  var4 = var6;
                  var5 = var7.getCount();
               }
            }

            if (field_1340.isToggled() && !(mc.screen instanceof InventoryScreen)) {
               for (int var9 = 0; var9 <= 3; var9++) {
                  ItemStack var11 = (ItemStack)mc.player.inventoryMenu.getCraftSlots().getItems().get(var9);
                  boolean var12 = ItemStack.isSameItemSameComponents(var11, var2);
                  if (!var11.isEmpty() && var12 && var11.getCount() > var5) {
                     var4 = var9 + 1;
                     var5 = var11.getCount();
                  }
               }
            }

            if (var4 == -1) {
               return false;
            } else {
               int var10 = mc.player.containerMenu.containerId;
               if (this.sort.getValue() && var5 != 1 && var2.getMaxStackSize() - var2.getCount() > var5) {
                  Hub.field_2611.method_154(true);
                  mc.gameMode.handleContainerInput(var10, var4, 0, ContainerInput.PICKUP, mc.player);
                  mc.gameMode.handleContainerInput(var10, var4, 1, ContainerInput.PICKUP, mc.player);
                  mc.gameMode.handleContainerInput(var10, InventoryUtil.getId(var1), 0, ContainerInput.PICKUP, mc.player);
                  Hub.field_2611.method_154(false);
               } else if (InventoryUtil.method_162()) {
                  mc.gameMode.handleContainerInput(var10, var4, 0, ContainerInput.QUICK_MOVE, mc.player);
               } else {
                  InventoryUtil.method_5(var4, InventoryUtil.getId(var1));
               }

               return true;
            }
         } else {
            return false;
         }
      }
   }
}
