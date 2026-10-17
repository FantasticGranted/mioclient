package me.mioclient.enum_;

import java.util.Collections;
import java.util.List;
import me.mioclient.api.Nameable;
import me.mioclient.api.MinecraftAccess;
import me.mioclient.internal.BlockUtil;
import me.mioclient.module.player.AutoFarmModule;
import net.minecraft.core.BlockPos;

/** AutoFarm targeting mode. */
public enum AutoFarmMode implements Nameable {
   CROSSHAIR("Crosshair") {
      @Override
      public List<BlockPos> method_2(AutoFarmModule var1) {
         return Collections.singletonList(BlockPos.containing(MinecraftAccess.mc.hitResult.getLocation()));
      }
   },
   SPHERE("Sphere") {
      @Override
      public List<BlockPos> method_2(AutoFarmModule var1) {
         return BlockUtil.method_2(MinecraftAccess.mc.player.position(), var1.range.getValue(), true);
      }
   };

   public final String displayName;

    AutoFarmMode(String var3) {
      this.displayName = var3;
   }

   @Override
   public String getName() {
      return this.displayName;
   }

   public abstract List<BlockPos> method_2(AutoFarmModule var1);
}
