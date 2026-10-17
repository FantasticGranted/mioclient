package me.mioclient.module.player;

import me.mioclient.enum_.NoInteractMode;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileItem;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import me.mioclient.nick.Settings;

public class NoInteractModule extends Module {
   public Setting<NoInteractMode> mode;
   public Setting<Boolean> onlyFood;
   public boolean field_1454;

   public NoInteractModule() {
      super("NoInteract", "Prevents you from interacting with tile entities.", Category.PLAYER);
      Settings.initialize(this);
   }

   public static boolean method_105(ItemStack var0) {
      return var0.has(DataComponents.FOOD)
         || var0.getItem() instanceof EnderpearlItem
         || var0.getItem() instanceof ProjectileItem
         || var0.getItem() instanceof ProjectileWeaponItem;
   }

   public static InteractionHand method_490() {
      ItemStack var0 = mc.player.getMainHandItem();
      ItemStack var1 = mc.player.getOffhandItem();
      return !method_105(var0) && method_105(var1) ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
   }

   public boolean method_2(ItemStack var1, BlockPos var2) {
      if (this.isToggled() && (!this.onlyFood.getValue() || var1.has(DataComponents.FOOD))) {
         BlockState var3 = mc.level.getBlockState(var2);
         return var3.is(Blocks.ANVIL) ? true : var3.hasBlockEntity() && !(var3.getBlock() instanceof BedBlock);
      } else {
         return false;
      }
   }
}
