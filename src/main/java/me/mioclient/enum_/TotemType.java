package me.mioclient.enum_;

import java.util.Set;
import me.mioclient.Hub;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.InventoryUtil;
import me.mioclient.module.combat.OffhandModule;
import me.mioclient.module.exploit.XCarryModule;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.gui.screens.inventory.ShulkerBoxScreen;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

public enum TotemType implements MinecraftAccess, Nameable {
   TOTEM("Totem", Items.TOTEM_OF_UNDYING),
   CRYSTAL("Crystal", Items.END_CRYSTAL),
   GAPPLE("Gapple", Items.GOLDEN_APPLE) {
      @Override
      public Item method_98(boolean var1) {
         return var1 ? super.method_98(true) : Items.ENCHANTED_GOLDEN_APPLE;
      }
   },
   CUSTOM("Custom", Items.SHIELD) {
      @Override
      public Item method_98(boolean var1) {
         Set var2 = TotemType.field_451.custom.getValue();
         return (Item)var2.stream().findAny().orElse(null);
      }
   };

   public static final XCarryModule field_450 = Hub.field_2595.method_78(XCarryModule.class);
   public static final OffhandModule field_451 = Hub.field_2595.method_78(OffhandModule.class);
   public final String field_452;
   public final Item field_453;

    TotemType(String var3, Item var4) {
      this.field_452 = var3;
      this.field_453 = var4;
   }

   public int method_186(boolean var1) {
      Item var2 = this.method_98(var1);
      if (var2 != null && !mc.player.getOffhandItem().is(var2)) {
         if (mc.screen instanceof ShulkerBoxScreen || mc.screen instanceof ContainerScreen) {
            for (Slot var4 : MinecraftAccess.mc.player.containerMenu.slots) {
               if (var4.getItem().is(var2)) {
                  return var4.index;
               }
            }
         }

         return InventoryUtil.method_2(var2, field_450.isToggled());
      } else {
         return -1;
      }
   }

   public Item method_98(boolean var1) {
      return this.field_453;
   }

   @Override
   public String getName() {
      return this.field_452;
   }
}
