package me.mioclient.module.player;

import me.mioclient.event.PlayerTickEvent;
import me.mioclient.event.PostTickEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.mixin.ducks.DuckMinecraftClient;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import me.mioclient.setting.Setting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ExperienceBottleItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import me.mioclient.nick.Settings;

public class FastPlaceModule extends Module {
   public Setting<Integer> delay;
   public Setting<Boolean> exp;
   public Setting<Boolean> rockets;
   public Setting<Boolean> blocks;
   public Setting<Boolean> enderChests;
   public Setting<Boolean> all;
   public Setting<Boolean> autoPlace;

   public FastPlaceModule() {
      super("FastPlace", "Removes the right-click delay for some items.", Category.PLAYER);
      Settings.initialize(this);
      this.setDrawn(false);
   }

   @Subscribe
   public void method_2(PostTickEvent var1) {
      DuckMinecraftClient var2 = (DuckMinecraftClient)(Object)mc;
      if (!mc.player.getMainHandItem().has(DataComponents.FOOD)) {
         if (this.autoPlace.getValue() && var2.getItemUseCooldown() == 0) {
            var2.interact();
         }
      }
   }

   @Subscribe
   public void method_2(PlayerTickEvent var1) {
      if (!this.method_535()) {
         Item var2 = mc.player.getMainHandItem().getItem();
         if (var2 instanceof ExperienceBottleItem && this.exp.getValue()
            || var2 instanceof FireworkRocketItem && this.rockets.getValue()
            || var2 instanceof BlockItem && this.blocks.getValue()
            || var2 == Items.ENDER_CHEST && this.enderChests.getValue()
            || this.all.getValue()) {
            ((DuckMinecraftClient)(Object)mc).setItemUseCooldown(Math.min(((DuckMinecraftClient)(Object)mc).getItemUseCooldown(), this.delay.getValue()));
         }
      }
   }
}
