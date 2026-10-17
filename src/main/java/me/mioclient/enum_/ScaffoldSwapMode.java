package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.InventoryUtil;

/** Scaffold block-swap mode: none or normal. */
public enum ScaffoldSwapMode implements MinecraftAccess, Nameable {
   NONE("None"),
   NORMAL("Normal") {
      @Override
      public void method_9(int var1, Runnable var2) {
         InventoryUtil.method_16(var1);
         var2.run();
      }
   },
   SILENT("Silent") {
      @Override
      public void method_9(int var1, Runnable var2) {
         int var3 = mc.player.getInventory().selected;
         InventoryUtil.method_16(var1);
         var2.run();
         InventoryUtil.method_16(var3);
      }
   };

   public final String displayName;

    ScaffoldSwapMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public void method_9(int var1, Runnable var2) {
      if (mc.player.getInventory().selected == var1) {
         var2.run();
      }
   }
}
