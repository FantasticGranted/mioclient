package me.mioclient.module.player;

import me.mioclient.Hub;
import me.mioclient.enum_.AutoEatSort;
import me.mioclient.enum_.PreType;
import me.mioclient.event.Event_19;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.EntityUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class AutoEatModule extends Module {
   public Setting<Integer> health;
   public Setting<Integer> hunger;
   public Setting<Boolean> pauseBaritone;
   public Setting<Boolean> autoSwap;
   public Setting<AutoEatSort> sort;
   public Setting<Boolean> swapBack;
   public Setting<Boolean> autoEscape;
   public Setting<Boolean> autoDisable;
   public int field_3076;
   public boolean field_3077;
   public int field_3078;

   public AutoEatModule() {
      super("AutoEat", "Eats your food for you.", Category.PLAYER);
      Settings.initialize(this);
      this.pauseBaritone.method_334();
   }

   @Override
   public void onToggle() {
      this.method_84(false);
      this.field_3076 = -1;
      this.field_3078 = -1;
   }

   @Override
   public void onDisable() {
      mc.options.keyUse.setDown(false);
      Hub.field_2630.method_9(this);
   }

   @Subscribe
   public void method_5(Event_19 var1) {
      if (var1.method_213() != PreType.POST) {
         Hub.field_2630.method_9(this);
         if (!this.autoSwap.getValue() && this.field_3077 && !mc.player.getMainHandItem().has(DataComponents.FOOD)) {
            mc.gameMode.releaseUsingItem(mc.player);
            this.method_84(false);
         } else {
            int var2 = this.method_891();
            if ((var2 == -1 || !mc.player.getInventory().getItem(var2).is(Items.CHORUS_FRUIT)) && this.method_893()) {
               int var3 = InventoryUtil.method_9(Items.CHORUS_FRUIT);
               if (var3 != -1) {
                  var2 = -1;
                  this.field_3078 = var3;
                  this.method_78(this.field_3078);
               }
            }

            if (this.method_894() && !this.method_893()) {
               if (this.field_3077) {
                  mc.gameMode.releaseUsingItem(mc.player);
                  this.method_84(false);
                  if (this.autoSwap.getValue() && this.swapBack.getValue() && this.field_3076 != -1) {
                     InventoryUtil.method_16(this.field_3076);
                  }

                  if (this.field_3078 != -1) {
                     this.method_78(this.field_3078);
                     this.field_3078 = -1;
                  }

                  if (this.autoDisable.getValue() && this.autoEscape.getValue()) {
                     this.disable();
                  }

                  this.field_3076 = -1;
               }
            } else {
               boolean var5 = var2 == 40;
               if (this.autoSwap.getValue() && !var5 && var2 != -1 && mc.player.getInventory().selected != var2) {
                  this.field_3076 = mc.player.getInventory().selected;
                  InventoryUtil.method_16(var2);
               }

               ItemStack var4 = mc.player.getMainHandItem();
               if (!mc.player.isUsingItem() && (var5 || var4.has(DataComponents.FOOD))) {
                  mc.gameMode.useItem(mc.player, var5 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);
                  this.method_84(true);
               }

               if (this.pauseBaritone.getValue() && mc.player.isUsingItem()) {
                  Hub.field_2630.method_2(this);
               }
            }
         }
      }
   }

   public int method_891() {
      double var1 = -1.0;
      int var3 = -1;

      for (int var4 = 0; var4 < 10; var4++) {
         if (var4 == 9) {
            var4 = 40;
         }

         ItemStack var5 = mc.player.getInventory().getItem(var4);
         FoodProperties var6 = (FoodProperties)var5.get(DataComponents.FOOD);
         if (var6 != null && (var6.canAlwaysEat() || mc.player.getFoodData().getFoodLevel() < 20)) {
            double var7 = this.sort.getValue().method_2(var5);
            if (var7 > var1 || var7 == var1 && var4 == mc.player.getInventory().selected) {
               var1 = var7;
               var3 = var4;
            }

            if (var5.getItem() == Items.CHORUS_FRUIT && this.method_893()) {
               var3 = var4;
               break;
            }
         }
      }

      return var3;
   }

   public boolean method_892() {
      return this.field_3077;
   }

   public boolean method_893() {
      if (!this.autoEscape.getValue()) {
         return false;
      } else {
         Player var1 = null;

         for (Player var3 : mc.level.players()) {
            if (var3.distanceTo(mc.player) <= 12F && !Hub.field_2603.getId(var3) && var3 != mc.player) {
               var1 = var3;
               break;
            }
         }

         return var1 != null && mc.player.tickCount <= 50;
      }
   }

   public boolean method_894() {
      return (this.hunger.getValue() == -1 || mc.player.getFoodData().getFoodLevel() > this.hunger.getValue())
         && (this.health.getValue() == 0 || EntityUtil.method_76() > (float)this.health.getValue().intValue());
   }

   public void method_84(boolean var1) {
      if (this.field_3077 != var1) {
         this.field_3077 = var1;
      }
   }

   public void method_78(int var1) {
      mc.gameMode
         .handleContainerInput(mc.player.containerMenu.containerId, var1, mc.player.getInventory().selected, ContainerInput.SWAP, mc.player);
   }
}
