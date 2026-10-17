package me.mioclient.module.combat;

import java.util.function.Predicate;
import me.mioclient.Hub;
import me.mioclient.event.FrameRenderEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.KeybindUtil;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.internal.Timer;
import me.mioclient.internal.EntityUtil;
import me.mioclient.internal.Class_0509;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.player.NoInteractModule;
import me.mioclient.setting.Setting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class MainhandModule extends Module {
   public static final NoInteractModule field_3096 = Hub.field_2595.method_78(NoInteractModule.class);
   public Setting<Integer> slot;
   public Setting<Integer> delay;
   public Setting<Boolean> autoSelect;
   public Setting<Integer> delay2;
   public Setting<Boolean> lethal;
   public Setting<Float> health;
   public Setting<Boolean> gappleBind;
   public Setting<Float> safe;
   public final Timer field_3105;
   public final Timer field_3106;
   public final Timer field_3107;
   public boolean field_1982;
   public int field_1071;
   public final Class_0509 field_3108;

   public MainhandModule() {
      super("Mainhand", "Auto totem for main hand.", Category.COMBAT);
      Settings.initialize(this);
      this.field_3105 = new Timer();
      this.field_3106 = new Timer();
      this.field_3107 = new Timer();
      this.field_3108 = new Class_0509() {
         @Override
         public boolean method_206() {
            return MainhandModule.this.lethal.getValue() && MainhandModule.this.autoSelect.getValue();
         }
      };
      this.delay2.method_31("SwapDelay");
   }

   @Override
   public void onEnable() {
      this.field_1071 = -1;
   }

   @Override
   public String getInfo() {
      int var1 = mc.player.getInventory().items.stream().filter(var1x -> var1x.getItem() == this.method_903()).mapToInt(ItemStack::getCount).sum();
      return String.valueOf(var1);
   }

   @Subscribe
   public void method_2(InteractBlockEvent var1) {
      boolean var2 = BlockUtil.method_2(var1.method_382()) && !mc.player.isShiftKeyDown() && !field_3096.isToggled();
      if (var2 && !var1.method_464()) {
         this.field_3107.reset();
      }
   }

   @Subscribe(
      method_800 = 100
   )
   public void method_9(FrameRenderEvent var1) {
      if (!this.method_535()) {
         this.field_1982 = var1.method_93();
         this.field_3108.method_142();
         if (mc.player.containerMenu == mc.player.inventoryMenu) {
            this.method_902();
            Item var2 = this.method_903();
            if (var2 != mc.player.getInventory().getItem(this.method_905()).getItem()) {
               if (mc.player.containerMenu.getCarried().isEmpty()) {
                  this.method_36(var2);
               }
            }
         }
      }
   }

   public void method_36(Item var1) {
      int var2 = InventoryUtil.method_2((Predicate<ItemStack>)(var1x -> var1x.is(var1)), true);
      if (this.field_1071 != -1 && mc.player.getInventory().getItem(this.field_1071).is(var1) && var2 != -1) {
         var2 = this.field_1071;
      }

      if (var2 != -1) {
         if (var2 < mc.player.containerMenu.slots.size()) {
            ItemStack var3 = mc.player.containerMenu.getSlot(var2).getItem();
            if (!var3.has(DataComponents.FOOD) || this.field_1982) {
               if (this.field_3105.method_5((long)this.delay.getValue().intValue()) && this.field_3107.method_9(200L)) {
                  InventoryUtil.method_9(var2, this.method_905());
                  this.field_1071 = var2;
               }

               if (KeybindUtil.method_2(mc.options.keyUse) && var3.has(DataComponents.FOOD)) {
                  mc.gameMode.useItem(mc.player, InteractionHand.OFF_HAND);
               }
            }
         }
      }
   }

   public void method_902() {
      if (this.autoSelect.getValue()) {
         Item var1 = mc.player.getInventory().getItem(this.method_905()).getItem();
         if (var1 == Items.TOTEM_OF_UNDYING && (!mc.player.isUsingItem() || mc.player.getUsedItemHand() != InteractionHand.MAIN_HAND)) {
            if ((this.field_3108.method_538() || EntityUtil.method_76() <= this.health.getValue())
               && this.field_3106.method_5((long)this.delay2.getValue().intValue())) {
               InventoryUtil.method_16(this.method_905());
            }
         }
      }
   }

   public Item method_903() {
      boolean var1 = BlockUtil.method_2(mc.hitResult)
         && !mc.player.isShiftKeyDown()
         && !field_3096.isToggled()
         && (
            !mc.player.isUsingItem()
               || mc.player.getUsedItemHand() != InteractionHand.MAIN_HAND
               || !mc.player.getMainHandItem().has(DataComponents.FOOD)
         );
      int var2 = mc.player.getInventory().selected;
      return var2 == this.method_905()
            && this.gappleBind.getValue()
            && mc.options.keyUse.isDown()
            && this.method_904()
            && !var1
            && EntityUtil.method_76() >= this.safe.getValue()
         ? Items.ENCHANTED_GOLDEN_APPLE
         : Items.TOTEM_OF_UNDYING;
   }

   public boolean method_904() {
      ItemStack var1 = mc.player.getMainHandItem();
      return mc.player.getOffhandItem().has(DataComponents.FOOD)
         ? false
         : var1.is(Items.TOTEM_OF_UNDYING) || var1.is(Items.ENCHANTED_GOLDEN_APPLE);
   }

   public int method_905() {
      return this.slot.getValue() - 1;
   }
}
