package me.mioclient.module.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import me.mioclient.Hub;
import me.mioclient.enum_.ChestSearchMode;
import me.mioclient.enum_.SelfFillMode;
import me.mioclient.enum_.ChestStealMode;
import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.Event_42;
import me.mioclient.event.ScreenOpenPostEvent;
import me.mioclient.event.SlotClickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.internal.Timer;
import me.mioclient.internal.ItemUtil;
import me.mioclient.internal.ShulkerPreviewScreen;
import me.mioclient.internal.Class_0584;
import me.mioclient.internal.ItemMatcherProfile;
import me.mioclient.internal.Class_1069;
import me.mioclient.internal.Class_1308;
import me.mioclient.mixin.ducks.DuckHandledScreen;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.module.misc.ChestSearchBarModule;
import me.mioclient.record.Vec2i;
import me.mioclient.record.ItemMatcher;
import me.mioclient.record.ItemComponents;
import me.mioclient.setting.Setting;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.ShulkerBoxMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import me.mioclient.nick.Settings;

public class ChestStealerModule extends Module {
   public static ChestSearchBarModule field_3025 = Hub.field_2595.method_78(ChestSearchBarModule.class);
   public Setting<Set<Item>> whitelist;
   public Setting<Set<String>> names;
   public Setting<ChestStealMode> mode;
   public Setting<SelfFillMode> select;
   public Setting<ChestSearchMode> lookFor;
   public Setting<Boolean> searchBarSync;
   public Setting<Boolean> onlyFull;
   public Setting<Boolean> onlyButtons;
   public Setting<Boolean> animate;
   public Setting<Integer> delay;
   public Setting<Integer> timeout;
   public Setting<Integer> frequency;
   public final List<Class_0584> field_3212;
   public final Timer field_3213;
   public final Timer field_3214;
   public ChestStealMode field_3215;
   public boolean field_947;
   public Slot field_3216;

   public ChestStealerModule() {
      super("ChestStealer", "Steals items from the storages you open.", Category.PLAYER, "stealer");
      Settings.initialize(this);
      this.field_3212 = Collections.synchronizedList(new ArrayList<>());
      this.field_3213 = new Timer();
      this.field_3214 = new Timer();
      this.animate.method_9(this.field_3212::clear);
      this.mode.method_5(var1 -> !this.onlyButtons.getValue());
      ScreenEvents.AFTER_INIT.register(new Class_1308(this));
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if ((mc.screen instanceof ContainerScreen || mc.screen instanceof ShulkerBoxScreen)
         && this.field_3213.method_9((long)this.delay.getValue().intValue())
         && (!this.onlyButtons.getValue() || this.field_3215 != null)
         && this.field_3214.method_9((long)this.timeout.getValue().intValue())
         && !(mc.screen instanceof ShulkerPreviewScreen)) {
         ChestStealMode var2 = this.onlyButtons.getValue() ? this.field_3215 : this.mode.getValue();
         AbstractContainerMenu var3 = mc.player.containerMenu;
         int var4 = 0;
         int var5 = 27;
         int var6 = var3.containerId;
         if (mc.screen instanceof ContainerScreen var7) {
            var5 = 9 * ((ChestMenu)var7.getMenu()).getRowCount();
         }

         DuckHandledScreen var23 = (DuckHandledScreen)(Object)mc.screen;
         Map var24 = this.method_493(var5);
         if (var2 == ChestStealMode.FILL) {
            var4 = var5;
            var5 = 36;
         }

         int var9 = 0;

         for (int var10 = var4; var10 < var4 + var5; var10++) {
            ItemStack var11 = var3.getSlot(var10).getItem();
            if (!var11.isEmpty() && (!this.searchBarSync.getValue() || !field_3025.isToggled() || field_3025.match(var11, this.onlyFull.getValue()))) {
               boolean var12 = mc.screen instanceof ShulkerBoxScreen;
               if (this.lookFor.getValue() == ChestSearchMode.ITEM
                  ? this.select.getValue().method_2(var11.getItem(), this.whitelist)
                  : this.select.getValue().method_2(var11.getHoverName().getString().toLowerCase(Locale.ROOT), this.names)) {
                  List var13 = (List)var24.get(new ItemComponents(var11.getItem(), var11.getComponents()));
                  switch (var2) {
                     case DROP:
                        mc.gameMode.handleContainerInput(var6, var10, 1, ContainerInput.THROW, mc.player);
                        break;
                     case FILL:
                     case STEAL:
                        if (var12 && ItemUtil.method_29(var11.getItem())) {
                           continue;
                        }

                        this.field_3216 = null;
                        this.field_947 = true;
                        mc.gameMode.handleContainerInput(var6, var10, 0, ContainerInput.QUICK_MOVE, mc.player);
                        this.field_947 = false;
                        if (this.field_3216 != null) {
                           this.method_2(var3.getSlot(var10), this.field_3216, this.field_3216.getItem());
                        }
                        break;
                     case REFILL:
                        if (var13 == null) {
                           continue;
                        }

                        this.method_9(var10, var13);
                        var9 = 999;
                        break;
                     case REKIT:
                        if (var13 != null) {
                           this.method_9(var10, var13);
                           var9 = 999;
                        } else {
                           ItemMatcherProfile var14 = Hub.field_2625.method_487();
                           if (var14 == null) {
                              continue;
                           }

                           Slot var15 = var3.getSlot(var10);
                           boolean var16 = false;

                           for (Entry var18 : var14.method_906().entrySet()) {
                              if (((ItemMatcher)var18.getValue()).method_16(var11)) {
                                 boolean var19 = (Integer)var18.getKey() < 9;
                                 int var20 = var5 + (var19 ? (Integer)var18.getKey() + 27 : (Integer)var18.getKey() - 9);
                                 ItemStack var21 = var3.getSlot(var20).getItem();
                                 if (!((ItemMatcher)var18.getValue()).method_16(var21)
                                    && (!ItemUtil.method_29(var21.getItem()) || !(var3 instanceof ShulkerBoxMenu))) {
                                    Slot var22 = var3.getSlot(var20);
                                    this.method_2(var15, var22, var15.getItem());
                                    if (var19) {
                                       mc.gameMode.handleContainerInput(var6, var10, (Integer)var18.getKey(), ContainerInput.SWAP, mc.player);
                                    } else {
                                       mc.gameMode.handleContainerInput(var6, var10, 0, ContainerInput.PICKUP, mc.player);
                                       mc.gameMode.handleContainerInput(var6, var20, 0, ContainerInput.PICKUP, mc.player);
                                       mc.gameMode.handleContainerInput(var6, var10, 0, ContainerInput.PICKUP, mc.player);
                                    }

                                    var9 = 999;
                                    var16 = true;
                                    break;
                                 }
                              }
                           }

                           if (!var16) {
                              continue;
                           }
                        }
                  }

                  this.field_3213.reset();
                  if (++var9 >= this.frequency.getValue()) {
                     break;
                  }
               }
            }
         }
      }
   }

   @Subscribe
   public void method_2(ScreenOpenPostEvent var1) {
      this.field_3214.reset();
      this.field_3212.clear();
   }

   @Subscribe
   public void onRender(Event_42 var1) {
      synchronized (this.field_3212) {
         this.field_3212.removeIf(Class_0584::method_604);

         for (Class_0584 var4 : this.field_3212) {
            var4.method_5(var1.method_881());
         }
      }
   }

   @Subscribe
   public void method_2(SlotClickEvent var1) {
      if (this.field_947 && !var1.method_1006().getItem().isEmpty()) {
         this.field_3216 = var1.method_1006();
      }
   }

   public void method_9(int var1, List<Class_1069> var2) {
      Slot var3 = mc.player.containerMenu.getSlot(var1);
      if (var3.getItem().isStackable()) {
         int var4 = mc.player.containerMenu.containerId;
         int var5 = var3.getItem().getCount();
         int var6 = 0;
         mc.gameMode.handleContainerInput(var4, var1, 0, ContainerInput.PICKUP, mc.player);

         for (Class_1069 var8 : var2) {
            var3 = mc.player.containerMenu.getSlot(var8.method_34());
            mc.gameMode.handleContainerInput(var4, var8.method_34(), 0, ContainerInput.PICKUP, mc.player);
            int var9 = var8.method_954() - var8.method_955();
            var8.method_494(var8.method_955() + var5);
            var5 -= var9;
            this.method_2(mc.player.containerMenu.getSlot(var1), var3, var3.getItem().copyWithCount(var9));
            if (mc.player.containerMenu.getCarried().isEmpty() || ++var6 >= this.frequency.getValue()) {
               break;
            }
         }

         if (!mc.player.containerMenu.getCarried().isEmpty()) {
            mc.gameMode.handleContainerInput(var4, var1, 0, ContainerInput.PICKUP, mc.player);
         }
      }
   }

   public boolean method_157(ItemStack var1) {
      return !(mc.player.containerMenu instanceof ShulkerBoxMenu) ? true : !ItemUtil.method_29(var1.getItem());
   }

   public void method_2(Slot var1, Slot var2, ItemStack var3) {
      DuckHandledScreen var4 = (DuckHandledScreen)(Object)mc.screen;

      assert var4 != null;

      if (this.animate.getValue()) {
         this.field_3212
            .add(new Class_0584(var3, new Vec2i(var4.getX() + var1.x, var4.getY() + var1.y), new Vec2i(var4.getX() + var2.x, var4.getY() + var2.y)));
      }
   }

   public Map<ItemComponents, List<Class_1069>> method_493(int var1) {
      HashMap var2 = new HashMap();

      for (int var3 = 0; var3 < 36; var3++) {
         if (var3 + var1 < mc.player.containerMenu.slots.size()) {
            ItemStack var4 = mc.player.containerMenu.getSlot(var1 + var3).getItem();
            if (var4.getCount() < var4.getMaxStackSize() && !var4.isEmpty() && var4.isStackable()) {
               DataComponentMap var5 = var4.getComponents();
               ItemComponents var6 = new ItemComponents(var4.getItem(), var5);
               var2.putIfAbsent(var6, new ArrayList());
               int var7 = var4.getMaxStackSize();
               ((List)var2.get(var6)).add(new Class_1069(var1 + var3, var7, var4.getCount()));
            }
         }
      }

      return var2;
   }

   public void method_2(ChestStealMode var1) {
      this.field_3215 = var1;
   }

   public ChestStealMode method_933() {
      return this.field_3215;
   }
}
