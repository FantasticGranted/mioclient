package me.mioclient.module.player;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import me.mioclient.Hub;
import me.mioclient.enum_.Class_1172;
import me.mioclient.enum_.NoneType;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.ScreenOpenEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.RotationManager;
import me.mioclient.internal.InteractionUtil;
import me.mioclient.internal.BlockUtil;
import me.mioclient.internal.PacketSender;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.record.Pair;
import me.mioclient.setting.Setting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import me.mioclient.nick.Settings;

public class AutoCraftModule extends Module {
   public static final RecipeHolder<?> field_2467 = new RecipeHolder(net.minecraft.resources.ResourceKey.create(net.minecraft.core.registries.Registries.RECIPE, Identifier.parse("minecraft:firework_rocket_simple")), null);
   public static final List<Ingredient> field_2468 = List.of(
      Ingredient.of(new ItemLike[]{Items.PAPER}), Ingredient.of(new ItemLike[]{Items.GUNPOWDER})
   );
   public Setting<Set<Item>> items;
   public Setting<Set<Item>> recipeBlackList;
   public Setting<Integer> delay;
   public Setting<Integer> frequency;
   public Setting<NoneType> drop;
   public Setting<Boolean> craftAll;
   public Setting<Boolean> fastClose;
   public Setting<Boolean> autoOpen;
   public Setting<Float> range;
   public Setting<Boolean> raytrace;
   public Setting<Boolean> strictDirection;
   public Setting<Boolean> rotate;
   public Setting<Boolean> limit;
   public Setting<Integer> max;
   public Setting<Boolean> stackLimit;
   public final Timer field_2484;
   public final Timer field_2485;

   public AutoCraftModule() {
      super("AutoCraft", "Automatically crafts listed items.", Category.PLAYER, "craftbot");
      Settings.initialize(this);
      this.field_2484 = new Timer();
      this.field_2485 = new Timer();
   }

   @Subscribe
   public void method_9(PostTickEvent var1) {
      net.minecraft.world.item.crafting.display.RecipeDisplayEntry var2 = this.method_731();
      if (var2 != null) {
         if (this.autoOpen.getValue() && this.field_2485.method_9(500L) && !(mc.screen instanceof AbstractContainerScreen)) {
            Pair var3 = this.method_732();
            if (var3 != null) {
               if (this.rotate.getValue()) {
                  PacketSender.method_2(RotationManager.method_78(((BlockPos)var3.getLeft()).getCenter()), mc.player.onGround());
               }

               PacketSender.method_2(
                  InteractionHand.MAIN_HAND,
                  new BlockHitResult(((BlockPos)var3.getLeft()).getCenter(), (Direction)var3.getRight(), (BlockPos)var3.getLeft(), false)
               );
               PacketSender.method_9(InteractionHand.MAIN_HAND);
               this.field_2485.reset();
            }
         }

         if (mc.player.containerMenu instanceof CraftingMenu
            && !this.items.getValue().isEmpty()
            && this.field_2484.method_9((long)this.delay.getValue().intValue())) {
            CraftingMenu var13 = (CraftingMenu)mc.player.containerMenu;
            ItemStack var4 = var2.display().result().resolveForFirstStack(net.minecraft.world.item.crafting.display.SlotDisplayContext.fromLevel(mc.level));
            if (!var4.is(Items.FIREWORK_ROCKET)) {
               for (int var14 = 0; var14 < this.method_33(var4.getItem()); var14++) {
                  mc.gameMode.handlePlaceRecipe(var13.containerId, var2.id(), this.craftAll.getValue());
               }
            } else {
               int var5 = -1;
               int var6 = -1;

               for (int var7 = 10; var7 < 46; var7++) {
                  ItemStack var8 = var13.getSlot(var7).getItem();
                  if (var8.is(Items.GUNPOWDER) && var5 == -1) {
                     var5 = var7;
                  }

                  if (var8.is(Items.PAPER) && var6 == -1) {
                     var6 = var7;
                  }
               }

               if (var6 != -1 && var13.getSlot(1).getItem().isEmpty()) {
                  this.field_2484.reset();
                  InventoryUtil.method_5(var6, 1);
                  return;
               }

               int[] var15 = new int[]{2, 4, 5};
               boolean var16 = true;

               for (int var12 : var15) {
                  if (!var13.getSlot(var12).getItem().is(Items.GUNPOWDER)) {
                     var16 = false;
                  }
               }

               if (var5 != -1 && !var16) {
                  mc.gameMode.handleContainerInput(var13.containerId, var5, 0, ContainerInput.PICKUP, mc.player);

                  for (int var22 : var15) {
                     if (!var13.getSlot(var22).getItem().is(Items.GUNPOWDER)) {
                        if (!var13.getCarried().isEmpty()) {
                           mc.gameMode.handleContainerInput(var13.containerId, var22, 1, ContainerInput.PICKUP, mc.player);
                        }
                        break;
                     }
                  }

                  mc.gameMode.handleContainerInput(var13.containerId, var5, 0, ContainerInput.PICKUP, mc.player);
                  this.field_2484.reset();
                  return;
               }

               ItemStack var17 = var13.getSlot(0).getItem();
               Fireworks var19 = (Fireworks)var17.getOrDefault(
                  DataComponents.FIREWORKS, new Fireworks(0, Collections.emptyList())
               );
               if (!var17.is(Items.FIREWORK_ROCKET)
                  || var19.flightDuration() != 3
                  || !var16
                  || !this.field_2484.method_9((long)Math.max(200, this.delay.getValue()))) {
                  return;
               }
            }

            mc.gameMode.handleContainerInput(var13.containerId, 0, 1, this.drop.getValue().method_1103(), mc.player);
            if (this.drop.getValue() == NoneType.PICKUP && var4.getCount() + var13.getCarried().getCount() > var13.getCarried().getMaxStackSize()) {
               mc.gameMode.handleContainerInput(var13.containerId, -999, 0, ContainerInput.PICKUP, mc.player);
            }

            if (this.fastClose.getValue()) {
               Hub.field_2619.method_2(() -> {
                  int var0 = mc.player.containerMenu.containerId;
                  PacketSender.method_2(new ServerboundContainerClosePacket(var0));
                  mc.player.containerMenu = mc.player.inventoryMenu;
               }, 1);
            }

            this.field_2484.reset();
         }
      }
   }

   @Subscribe
   public void method_2(ScreenOpenEvent var1) {
      if (var1.method_562() instanceof CraftingScreen && this.fastClose.getValue()) {
         // TODO(runtime): Screen.init(Minecraft,int,int) removed in 26.1
         var1.method_463();
      }
   }

   public net.minecraft.world.item.crafting.display.RecipeDisplayEntry method_731() {
      if (this.items.getValue().isEmpty()) {
         return null;
      } else {
         net.minecraft.util.context.ContextMap var1 = net.minecraft.world.item.crafting.display.SlotDisplayContext.fromLevel(mc.level);

         for (net.minecraft.client.gui.screens.recipebook.RecipeCollection var2 : mc.player.getRecipeBook().getCollections()) {
            for (net.minecraft.world.item.crafting.display.RecipeDisplayEntry var3 : var2.getRecipes()) {
               if (!(var3.display() instanceof net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay)
                  && !(var3.display() instanceof net.minecraft.world.item.crafting.display.ShapelessCraftingRecipeDisplay)) {
                  continue;
               }

               List<ItemStack> var5 = var3.resultItems(var1);
               if (var5.isEmpty()) {
                  continue;
               }

               ItemStack var6 = var5.get(0);
               if (var6.getItem() != null && !(var6.getItem() instanceof SmithingTemplateItem)) {
                  Object var4 = var3.craftingRequirements().orElse(null);
                  if (var6.is(Items.FIREWORK_ROCKET)) {
                     var4 = field_2468;
                  }

                  if (this.items.getValue().contains(var6.getItem()) && var4 != null && this.method_9((Collection<Ingredient>)var4) && this.method_37(var6.getItem())) {
                     return var3;
                  }
               }
            }
         }

         return null;
      }
   }

   public boolean method_37(Item var1) {
      if (this.limit.getValue() && !this.craftAll.getValue()) {
         int var2 = 0;
         int var3 = this.max.getValue();
         if (this.stackLimit.getValue()) {
            var3 *= var1.getDefaultMaxStackSize();
         }

         for (ItemStack var5 : mc.player.getInventory().items) {
            if (var5.is(var1)) {
               var2 += var5.getCount();
            }
         }

         return var2 < var3;
      } else {
         return true;
      }
   }

   public int method_33(Item var1) {
      if (!this.limit.getValue()) {
         return this.frequency.getValue();
      } else {
         int var2 = 0;
         int var3 = this.max.getValue();
         if (this.stackLimit.getValue()) {
            var3 *= var1.getDefaultMaxStackSize();
         }

         for (ItemStack var5 : mc.player.getInventory().items) {
            if (var5.is(var1)) {
               var2 += var5.getCount();
            }
         }

         return Mth.clamp(var3 - var2, 0, this.frequency.getValue());
      }
   }

   public boolean method_9(Collection<Ingredient> var1) {
      HashMap<Item, Integer> var2 = new HashMap<>();

      for (Slot var4 : mc.player.containerMenu.slots) {
         var2.compute(
            var4.getItem().getItem(), (var1x, var2x) -> var1x != null && var2x != null ? var4.getItem().getCount() + var2x : var4.getItem().getCount()
         );
      }

      for (Ingredient var12 : var1) {
         if (!var12.isEmpty()) {
            boolean var5 = false;

            for (ItemStack var9 : var12.items().map(net.minecraft.world.item.ItemStack::new).toList()) {
               if (this.recipeBlackList.getValue().contains(var9.getItem())) {
                  return false;
               }

               if (var2.containsKey(var9.getItem())) {
                  Integer var10 = (Integer)var2.get(var9.getItem());
                  if (var10 - var9.getCount() >= 0) {
                     var2.replace(var9.getItem(), var10 - var9.getCount());
                     var5 = true;
                     break;
                  }
               }
            }

            if (!var5) {
               return false;
            }
         }
      }

      return true;
   }

   public Pair<BlockPos, Direction> method_732() {
      for (BlockPos var2 : BlockUtil.method_2(mc.player.getEyePosition(), this.range.getValue(), true)) {
         if (mc.level.getBlockState(var2).is(Blocks.CRAFTING_TABLE)
            && (BlockUtil.method_2(Class_1172.field_3634.method_38(var2)) || !this.raytrace.getValue())) {
            List var3 = InteractionUtil.method_39(var2);
            if (!var3.isEmpty() || !this.strictDirection.getValue()) {
               return new Pair<>(var2, var3.isEmpty() ? Direction.UP : (Direction)var3.get(0));
            }
         }
      }

      return null;
   }
}
