package me.mioclient.api;

import me.mioclient.record.IntPair;
import net.minecraft.world.item.Item;

/** Strategy for moving an item to a target inventory region: locate an item's slot, and swap to / restore from it. */
public interface SwapHandler extends MinecraftAccess {
   void method_2(IntPair var1);

   void method_9(IntPair var1);

   IntPair method_31(Item var1);
}
