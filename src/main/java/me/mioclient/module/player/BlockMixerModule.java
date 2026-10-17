package me.mioclient.module.player;

import java.util.ArrayList;
import java.util.Random;
import me.mioclient.event.PacketSendEvent;
import me.mioclient.event.InteractBlockEvent;
import me.mioclient.event.Subscribe;
import me.mioclient.module.Category;
import me.mioclient.module.Module;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;

public class BlockMixerModule extends Module {
   public final Random field_3750 = new Random();

   public BlockMixerModule() {
      super("BlockMixer", "Picks a random block from your hotbar as you build.", Category.PLAYER);
   }

   @Subscribe
   public void method_9(InteractBlockEvent var1) {
      if (mc.player.getMainHandItem().getItem() instanceof BlockItem) {
         this.method_1055();
      }
   }

   @Subscribe
   public void method_2(PacketSendEvent var1) {
      if (var1.method_127() instanceof ServerboundUseItemOnPacket && mc.player.getMainHandItem().getItem() instanceof BlockItem) {
         this.method_1055();
      }
   }

   public void method_1055() {
      ArrayList var1 = new ArrayList();

      for (int var2 = 0; var2 < 9; var2++) {
         ItemStack var3 = mc.player.getInventory().getItem(var2);
         if (!var3.isEmpty() && var3.getItem() instanceof BlockItem) {
            var1.add(var2);
         }
      }

      if (var1.size() >= 2) {
         mc.player.getInventory().selected = (Integer)var1.get(this.field_3750.nextInt(var1.size()));
      }
   }
}
