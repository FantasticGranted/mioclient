package me.mioclient.internal;
import net.minecraft.world.item.Items;

import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.mixin.ducks.DuckInteractionManager;
import me.mioclient.module.player.ItemSaverModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Inventory/slot helpers: select a hotbar slot, swap and click slots through the
 * interaction manager for item management.
 */
public class InventoryUtil implements MinecraftAccess {
   public static ItemSaverModule field_405 = Hub.field_2595.method_78(ItemSaverModule.class);

   public InventoryUtil() {
      super();
   }

   public static void method_78(int var0) {
      if (var0 >= 0 && var0 <= 8) {
         mc.player.getInventory().selected = var0;
         ((DuckInteractionManager)(Object)mc.gameMode).sync();
      }
   }

   public static void method_39(int var0) {
      if (var0 >= 0) {
         mc.gameMode
            .handleContainerInput(
               mc.player.containerMenu.containerId,
               getId(var0),
               mc.player.getInventory().selected,
               ContainerInput.SWAP,
               mc.player
            );
      }
   }

   public static void method_9(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0) {
         int var2 = mc.player.containerMenu.containerId;
         mc.gameMode.handleContainerInput(var2, var0, var1, ContainerInput.SWAP, mc.player);
      }
   }

   public static void method_5(int var0, int var1) {
      if (var0 != -1 && var1 != -1) {
         mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var0, 0, ContainerInput.PICKUP, mc.player);
         mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var1, 0, ContainerInput.PICKUP, mc.player);
         if (!mc.player.containerMenu.getCarried().isEmpty()) {
            mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var0, 0, ContainerInput.PICKUP, mc.player);
         }
      }
   }

   public static int method_2(Item var0, boolean var1) {
      return method_2(var0, var0x -> true, var1);
   }

   public static int method_9(Item var0) {
      return method_2(var0, var0x -> true, false);
   }

   public static int method_2(Item var0, Predicate<ItemStack> var1, boolean var2) {
      if (!Hub.field_2611.method_842() && var0.equals(mc.player.getOffhandItem().getItem()) && var1.test(mc.player.getOffhandItem())) {
         return -1;
      } else {
         for (int var3 = 36; var3 >= 0; var3--) {
            ItemStack var4 = mc.player.getInventory().getItem(var3);
            if (var4.getItem() == var0 && var1.test(var4)) {
               if (var3 < 9) {
                  var3 += 36;
               }

               return var3;
            }
         }

         if (var2) {
            for (int var5 = 0; var5 <= 3; var5++) {
               ItemStack var6 = (ItemStack)mc.player.inventoryMenu.getCraftSlots().getItems().get(var5);
               if (var6.getItem() == var0 && var1.test(var6)) {
                  return var5 + 1;
               }
            }
         }

         return -1;
      }
   }

   public static int method_2(Predicate<ItemStack> var0, boolean var1) {
      for (int var2 = 36; var2 >= (var1 ? 9 : 0); var2--) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (var0.test(var3)) {
            return var2;
         }
      }

      return -1;
   }

   public static int method_2(Item... var0) {
      return method_7((Predicate<ItemStack>)(var1 -> {
         for (Item var5 : var0) {
            if (var5.equals(var1.getItem())) {
               return true;
            }
         }

         return false;
      }));
   }

   public static int method_5(Item var0) {
      return method_7((Predicate<ItemStack>)(var1 -> var1.getItem().equals(var0)));
   }

   public static int method_7(Predicate<ItemStack> var0) {
      for (int var1 = 0; var1 < 9; var1++) {
         ItemStack var2 = mc.player.getInventory().getItem(var1);
         if (var0.test(var2)) {
            return var1;
         }
      }

      return -1;
   }

   public static int method_29(Predicate<ItemStack> var0) {
      int var1 = 0;

      for (int var2 = 40; var2 >= 0; var2--) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (var0.test(var3)) {
            var1 += var3.getCount();
         }
      }

      for (int var4 = 0; var4 <= 3; var4++) {
         ItemStack var5 = (ItemStack)mc.player.inventoryMenu.getCraftSlots().getItems().get(var4);
         if (var0.test(var5)) {
            var1 += var5.getCount();
         }
      }

      return var1;
   }

   public static InteractionHand method_7(Item var0) {
      if (mc.player.getOffhandItem().getItem() == var0) {
         return InteractionHand.OFF_HAND;
      } else {
         return mc.player.getMainHandItem().getItem() == var0 ? InteractionHand.MAIN_HAND : null;
      }
   }

   public static float method_2(Player var0) {
      float var1 = 1e+02F;

      for (ItemStack var3 : java.util.List.of(var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST), var0.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD))) {
         float var4 = method_29(var3);
         if (var4 < var1) {
            var1 = var4;
         }
      }

      return var1;
   }

   public static float method_29(ItemStack var0) {
      float var1 = ((float)var0.getMaxDamage() - (float)var0.getDamageValue()) / (float)var0.getMaxDamage();
      float var2 = 1F - var1;
      return (float)(100 - (int)(var2 * 1e+02F));
   }

   public static int getId(int var0) {
      return var0 > -1 && var0 < 9 ? 36 + var0 : var0;
   }

   public static boolean method_161() {
      for (int var0 = 36; var0 >= 0; var0--) {
         ItemStack var1 = mc.player.getInventory().getItem(var0);
         if (var1.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public static boolean method_162() {
      for (int var0 = 0; var0 < 9; var0++) {
         ItemStack var1 = mc.player.getInventory().getItem(var0);
         if (var1.isEmpty()) {
            return false;
         }
      }

      return true;
   }

   public static void method_16(int var0) {
      if (var0 != -1) {
         mc.player.getInventory().selected = var0;
         ((DuckInteractionManager)(Object)mc.gameMode).sync();
      }
   }

   public static boolean method_2(EquipmentSlot var0, ItemStack var1) {
      if (var0 == null) {
         return false;
      } else {
         net.minecraft.world.item.equipment.Equippable var2 = var1.get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
         return var2 != null && var2.slot() == var0 || var1.is(Items.ELYTRA) && var0 == EquipmentSlot.CHEST;
      }
   }

   public static EquipmentSlot method_31(int var0) {
      return switch (var0) {
         case 36 -> EquipmentSlot.FEET;
         case 37 -> EquipmentSlot.LEGS;
         case 38 -> EquipmentSlot.CHEST;
         case 39 -> EquipmentSlot.HEAD;
         default -> null;
      };
   }

   public static int method_29(BlockPos var0, boolean var1) {
      double var2 = -1.0;
      int var4 = -1;
      BlockState var5 = mc.level.getBlockState(var0);

      for (int var6 = 0; var6 < (!var1 ? 36 : 9); var6++) {
         ItemStack var7 = mc.player.getInventory().getItem(var6);
         if (!field_405.isToggled() || ItemSaverModule.method_101(var7)) {
            double var8 = (double)var7.getDestroySpeed(var5);
            if (var8 > var2) {
               var2 = var8;
               var4 = var6;
            } else if (var6 == mc.player.getInventory().selected && var8 == var2) {
               var4 = var6;
            }
         }
      }

      return var4;
   }

   public static boolean method_29(Item var0) {
      if (var0 instanceof BlockItem var1 && var1.getBlock() instanceof ShulkerBoxBlock) {
         return true;
      }

      return false;
   }

   public static boolean isArmor(ItemStack var0) {
      net.minecraft.world.item.equipment.Equippable var1 = var0.get(net.minecraft.core.component.DataComponents.EQUIPPABLE);
      return var1 != null && var1.slot().isArmor();
   }

   public static boolean isSword(ItemStack var0) {
      return var0.has(net.minecraft.core.component.DataComponents.WEAPON)
         && !(var0.getItem() instanceof net.minecraft.world.item.AxeItem)
         && !(var0.getItem() instanceof net.minecraft.world.item.MaceItem)
         && !(var0.getItem() instanceof net.minecraft.world.item.TridentItem)
         && !(var0.getItem() instanceof net.minecraft.world.item.ProjectileWeaponItem);
   }

   public static boolean isSwordItem(Item var0) {
      return isSword(new ItemStack(var0));
   }

   public static boolean isPickaxe(Item var0) {
      return var0.getDescriptionId().contains("pickaxe");
   }

   public static boolean isTiered(ItemStack var0) {
      return var0.get(net.minecraft.core.component.DataComponents.WEAPON) != null
         || var0.get(net.minecraft.core.component.DataComponents.TOOL) != null;
   }

   public static double armorDefense(ItemStack var0, EquipmentSlot var1) {
      net.minecraft.world.item.component.ItemAttributeModifiers var2 = var0.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
      return var2 == null ? 0.0 : var2.compute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR, 0.0, var1);
   }

   public static double armorToughness(ItemStack var0, EquipmentSlot var1) {
      net.minecraft.world.item.component.ItemAttributeModifiers var2 = var0.get(net.minecraft.core.component.DataComponents.ATTRIBUTE_MODIFIERS);
      return var2 == null ? 0.0 : var2.compute(net.minecraft.world.entity.ai.attributes.Attributes.ARMOR_TOUGHNESS, 0.0, var1);
   }

}
