package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

/** A placeable climbable/support block type with its display name and item. */
public enum ClimbableType implements Nameable {
   LADDER("Ladder", Items.LADDER),
   VINE("Vine", Items.VINE),
   SCAFFOLDING("Scaffolding", Items.SCAFFOLDING),
   ITEMFRAME("ItemFrame", Items.ITEM_FRAME);

   public final String displayName;
   public final Item item;

    ClimbableType(String var3, Item var4) {
      this.displayName = var3;
      this.item = var4;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public Item method_903() {
      return this.item;
   }
}
