package me.mioclient.internal;

import me.mioclient.api.SwapHandler;
import me.mioclient.record.IntPair;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.item.Item;

public class Class_1259 implements SwapHandler {
   public Class_1259() {
      super();
   }

   @Override
   public void method_2(IntPair var1) {
      mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var1.getSecond(), 40, ContainerInput.SWAP, mc.player);
   }

   @Override
   public void method_9(IntPair var1) {
      mc.gameMode.handleContainerInput(mc.player.containerMenu.containerId, var1.getSecond(), 40, ContainerInput.SWAP, mc.player);
   }

   @Override
   public IntPair method_31(Item var1) {
      return new IntPair(InventoryUtil.method_9(var1), 40);
   }
}
