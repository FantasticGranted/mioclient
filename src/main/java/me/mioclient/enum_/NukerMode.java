package me.mioclient.enum_;

import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.module.player.NukerModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ShulkerBoxBlock;
import net.minecraft.world.level.block.state.properties.BedPart;

/** Nuker mode. */
public enum NukerMode implements Nameable {
   SHULKERS("Shulkers") {
      @Override
      public boolean method_2(BlockPos var1, Block var2, NukerModule var3) {
         return var2 instanceof ShulkerBoxBlock;
      }
   },
   BEDS("Bed") {
      @Override
      public boolean method_2(BlockPos var1, Block var2, NukerModule var3) {
         return var2 instanceof BedBlock && MinecraftAccess.mc.level.getBlockState(var1).getValue(BedBlock.PART) == BedPart.HEAD;
      }
   },
   WHITELIST("WhiteList") {
      @Override
      public boolean method_2(BlockPos var1, Block var2, NukerModule var3) {
         return var3.whitelist.getValue().contains(var2);
      }
   },
   BLACKLIST("BlackList") {
      @Override
      public boolean method_2(BlockPos var1, Block var2, NukerModule var3) {
         return !WHITELIST.method_2(var1, var2, var3);
      }
   };

   public final String displayName;

    NukerMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public abstract boolean method_2(BlockPos var1, Block var2, NukerModule var3);
}
