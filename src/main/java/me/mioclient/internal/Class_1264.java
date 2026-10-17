package me.mioclient.internal;

import me.mioclient.Hub;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.enum_.ArmorSlot;
import me.mioclient.module.combat.AutoArmorModule;
import net.minecraft.world.inventory.ContainerInput;

public class Class_1264 {
   public boolean field_3965 = false;
   public boolean field_3966;
   public final ArmorSlot field_3967;
   public final int field_3968;

   public Class_1264(AutoArmorModule var1, ArmorSlot var2, int var3) {
      super();
      this.field_3967 = var2;
      this.field_3968 = var3;
   }

   public void method_222(boolean var1) {
      this.field_3966 = var1;
   }

   public boolean method_497() {
      return this.field_3965;
   }

   public void method_273() {
      if (this.field_3966) {
         MinecraftAccess.mc
            .gameMode
            .handleContainerInput(
               MinecraftAccess.mc.player.containerMenu.containerId,
               8 - this.field_3967.field_1822,
               0,
               ContainerInput.QUICK_MOVE,
               MinecraftAccess.mc.player
            );
         this.field_3965 = true;
      } else {
         int var1 = InventoryUtil.getId(this.field_3968);
         Hub.field_2611.method_154(true);
         InventoryUtil.method_5(var1, 8 - this.field_3967.getArmorIndex());
         Hub.field_2611.method_154(false);
         this.field_3965 = true;
      }
   }
}
