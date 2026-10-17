package me.mioclient.module.player;

import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.enum_.AutoFarmMode;
import me.mioclient.event.Event_7;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SugarCaneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import me.mioclient.nick.Settings;

public class AutoFarmModule extends Module {
   public Setting<AutoFarmMode> mode;
   public Setting<Float> range;
   public Setting<Integer> bPT;
   public Setting<Boolean> place;
   public Setting<Boolean> rotate;
   public Setting<Boolean> fertilize;
   public Setting<Boolean> remove;
   public Setting<Float> removeDelay;
   public Setting<Boolean> onlyGrown;
   public Setting<Boolean> crops;
   public Setting<Boolean> wheat;
   public Setting<Boolean> carrots;
   public Setting<Boolean> potatoes;
   public Setting<Boolean> beetRoots;
   public Setting<Boolean> sugarCane;
   public Setting<Boolean> trees;
   public final Timer field_2261;
   public final Timer field_2262;
   public int field_2263;

   public AutoFarmModule() {
      super("AutoFarm", "Farms your crops for you.", Category.PLAYER);
      Settings.initialize(this);
      this.field_2261 = new Timer();
      this.field_2262 = new Timer();
   }

   @Subscribe
   public void method_9(Event_7 var1) {
      for (BlockPos var3 : this.mode.getValue().method_2(this)) {
         if (this.field_2263 >= this.bPT.getValue()) {
            break;
         }

         this.method_39(var3, mc.level.isBlockInLine(new ClipBlockStateContext(mc.player.getEyePosition(), var3.getCenter(), var0 -> true)).getDirection());
      }

      this.field_2263 = 0;
   }

   public void method_39(BlockPos var1, Direction var2) {
      BlockState var3 = mc.level.getBlockState(var1);
      boolean var4 = var3.is(Blocks.SUGAR_CANE);
      InteractionHand var5 = this.method_38(var1x -> this.method_34(var1x.getItem()));
      if (this.place.getValue() && var5 != null && this.method_2(var1, var3, mc.player.getItemInHand(var5).getItem())) {
         this.method_2(var1, var5);
      } else if (this.method_34(var3.getBlock().asItem())) {
         boolean var6 = this.method_2(var3, var1);
         InteractionHand var7 = this.method_7(Items.BONE_MEAL);
         if (this.fertilize.getValue() && !var6 && var7 != null && !var4) {
            this.method_9(var1, var7);
         } else {
            this.method_9(var1, var2, var6);
         }
      }
   }

   public void method_9(BlockPos var1, Direction var2, boolean var3) {
      if (!(mc.level.getBlockState(var1).getBlock() instanceof SaplingBlock)) {
         if (this.remove.getValue() && this.field_2261.method_2((double)this.removeDelay.getValue().floatValue(), TimeUnit.SECONDS)) {
            if (var3 || !this.onlyGrown.getValue()) {
               mc.gameMode.continueDestroyBlock(var1, var2);
               mc.player.swing(InteractionHand.MAIN_HAND);
               this.field_2262.reset();
               this.field_2263++;
               this.field_2261.reset();
               if (this.rotate.getValue()) {
                  Hub.field_2598.method_2(RotationManager.method_78(var1.getCenter()), 0);
               }
            }
         }
      }
   }

   public void method_2(BlockPos var1, InteractionHand var2) {
      mc.gameMode.useItemOn(mc.player, var2, new BlockHitResult(var1.getCenter(), Direction.UP, var1, false));
      mc.player.swing(var2);
      this.field_2262.reset();
      this.field_2263++;
      if (this.rotate.getValue()) {
         Hub.field_2598.method_2(RotationManager.method_78(var1.getCenter()), 0);
      }
   }

   public void method_9(BlockPos var1, InteractionHand var2) {
      if (this.rotate.getValue()) {
         Hub.field_2598.method_2(RotationManager.method_78(Vec3.atBottomCenterOf(var1)), 0);
      }

      mc.gameMode.useItemOn(mc.player, var2, new BlockHitResult(Vec3.atBottomCenterOf(var1), Direction.UP, var1, false));
      mc.player.swing(var2);
      this.field_2262.reset();
      this.field_2263++;
   }

   public boolean method_2(BlockPos var1, BlockState var2, Item var3) {
      if (var3 instanceof BlockItem var4) {
         if (var4.getBlock() instanceof CropBlock) {
            return var2.is(Blocks.FARMLAND) && mc.level.getBlockState(var1.above()).is(Blocks.AIR);
         }

         if (var4.getBlock() instanceof SugarCaneBlock) {
            if (!mc.level.getBlockState(var1.above()).is(Blocks.AIR)) {
               return false;
            }

            if (!var2.is(BlockTags.DIRT) && !var2.is(BlockTags.SAND)) {
               return false;
            }

            for (Direction var6 : Plane.HORIZONTAL) {
               FluidState var7 = mc.level.getFluidState(var1.relative(var6));
               var2 = mc.level.getBlockState(var1.relative(var6));
               if (var7.is(FluidTags.WATER) || var2.is(Blocks.FROSTED_ICE)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public boolean method_2(BlockState var1, BlockPos var2) {
      if (var1.getBlock() instanceof SaplingBlock) {
         return false;
      } else if (var1.getBlock() instanceof CropBlock var3) {
         return var3.getAge(var1) == var3.getMaxAge();
      } else {
         return var1.getBlock() instanceof SugarCaneBlock ? mc.level.getBlockState(var2.below()).is(Blocks.SUGAR_CANE) : false;
      }
   }

   public InteractionHand method_7(Item var1) {
      return this.method_38(var1x -> var1x.is(var1));
   }

   public InteractionHand method_38(Predicate<ItemStack> var1) {
      if (var1.test(mc.player.getMainHandItem())) {
         return InteractionHand.MAIN_HAND;
      } else {
         return var1.test(mc.player.getOffhandItem()) ? InteractionHand.OFF_HAND : null;
      }
   }

   public boolean method_34(Item var1) {
      if (var1 instanceof BlockItem var2 && var2.getBlock() instanceof SaplingBlock) {
         return this.trees.getValue();
      }

      return var1 == Items.WHEAT_SEEDS && this.wheat.getValue()
         || var1 == Items.CARROT && this.carrots.getValue()
         || var1 == Items.POTATO && this.potatoes.getValue()
         || var1 == Items.BEETROOT_SEEDS && this.beetRoots.getValue()
         || var1 == Items.SUGAR_CANE && this.sugarCane.getValue();
   }
}
