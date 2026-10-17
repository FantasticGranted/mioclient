package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import net.minecraft.world.inventory.ContainerInput;

public enum NoneType implements Nameable {
   NONE("None", ContainerInput.QUICK_MOVE),
   THROW("Throw", ContainerInput.THROW),
   PICKUP("Pickup", ContainerInput.PICKUP);

   public final String field_3962;
   public final ContainerInput field_3963;

    NoneType(String var3, ContainerInput var4) {
      this.field_3962 = var3;
      this.field_3963 = var4;
   }

   @Override
   public String getName() {
      return this.field_3962;
   }

   public ContainerInput method_1103() {
      return this.field_3963;
   }
}
